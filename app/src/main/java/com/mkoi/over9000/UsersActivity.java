package com.mkoi.over9000;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.mkoi.over9000.adapter.UserListAdapter;
import com.mkoi.over9000.handler.ConnectionHandler;
import com.mkoi.over9000.message.ConnectToUser;
import com.mkoi.over9000.model.User;
import com.mkoi.over9000.preferences.UserPreferences_;
import com.mkoi.over9000.secure.DiffieHellman;
import com.mkoi.over9000.socket.SocketConnection;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.List;

/**
 * Lista użytkowników
 */
@SuppressLint("Registered")
@EActivity(R.layout.activity_users)
public class UsersActivity extends Activity {
    public static final String LOG_TAG = "FriendsActivity";

    @ViewById
    ListView userList;

    @Bean
    UserListAdapter adapter;

    @Pref
    UserPreferences_ userPreferences;

    @Bean
    SocketConnection connection;

    @Bean
    ConnectionHandler connectionHandler;

    @Bean
    DiffieHellman diffieHellman;

    /**
     * Mapper POJO na JSON
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Dialog wyświetlany podczas podłączania
     */
    private Dialog waitForUser;

    /**
     * Podpięcie adaptera do listy
     */
    @AfterViews
    public void bindAdapter() {
        userList.setAdapter(adapter);
    }

    /**
     * Ustawienie handlera i tokenu na potrzeby socket.io
     */
    @AfterInject
    public void setupConnection() {
        connection.init(userPreferences.token().get());
        connection.setupConnectionHandler(connectionHandler);
    }

    /**
     * Pobranie listy użytkowników dostępnych na serwerze
     * @param jsonUsersArray
     */
    public void refreshList(String jsonUsersArray) {
        Log.d(LOG_TAG, "Refreshing user list");
        try {
            List<User> users = objectMapper.readValue(jsonUsersArray, new TypeReference<List<User>>(){});
            adapter.setUsers(users);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing users json", e);
        }
    }

    /**
     * Dodanie do listy nowego użytkownika
     * @param jsonUser
     */
    public void userConnected(String jsonUser) {
        Log.d(LOG_TAG, "Adding new user");
        try {
            User user = getUser(jsonUser);
            adapter.addUser(user);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing user json", e);
        }
    }

    /**
     * Mapowanie JSON na obiekt User
     * @param jsonUser użytkownik w postci obiektu JSON
     * @return pojo użytkownik
     * @throws IOException
     */
    private User getUser(String jsonUser) throws IOException {
        return objectMapper.readValue(jsonUser, User.class);
    }

    /**
     * Usunięcie użytkownika z listy kiedy się wyloguje
     * @param jsonUser użytkownik w JSON
     */
    public void userDisconnected(String jsonUser) {
        Log.d(LOG_TAG, "Removing user from list");
        try {
            User user = getUser(jsonUser);
            adapter.deleteUser(user);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing user json", e);
        }
    }

    /**
     * Rozłączenie użytkownika po wyjściu
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        connection.disconnect();
    }

    /**
     * Wybranie użytkownika do którego chcemy się podłączyć
     * @param user wybrany użytkownik
     */
    @ItemClick
    public void userListItemClicked(User user) {
        Log.d(LOG_TAG, "User requested a connection with "+user.getNick());
        waitForUser = new Dialog(this);
        waitForUser.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitForUser.setContentView(R.layout.wait_for_connection_dialog);
        waitForUser.show();
        sendConnectionRequest(user);
    }

    /**
     * Wysłanie żądania połączenia do użytkownika
     * @param user wybrany użytkownik
     */
    @Background
    public void sendConnectionRequest(User user) {
        ConnectToUser request = new ConnectToUser();
        try {
            diffieHellman.generateParameters();
            PublicKey key = diffieHellman.generateKeypair();
            request.setSocketId(user.getId());
            request.setKey(key.getEncoded());
            connection.connectToUser(request);
        } catch (NoSuchAlgorithmException | InvalidParameterSpecException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            Log.e(LOG_TAG, "Error while calculating DH", e);
            waitForUser.dismiss();
        }
    }

    /**
     * Przejście do kolejnej activity, jeżeli ktoś zaakceptował nasze połączenie
     * @param jsonUser użytkownik w JSON
     */
    public void connectionAccepted(String jsonUser) {
        Log.d(LOG_TAG, "User accepted connection");
        try {
            User user = getUser(jsonUser);
            diffieHellman.finishKeyAgreement(user.getKey());
            waitForUser.dismiss();
            startChatActivity(user, diffieHellman.getSecret());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing user json", e);
        } catch (InvalidKeySpecException | InvalidKeyException | NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Error while calculating DH", e);
        }
    }

    /**
     * Połączenie odrzucone
     * @param jsonUser użytkownik w JSON
     */
    public void connectionRejected(String jsonUser) {
        Log.d(LOG_TAG, "User rejected connection");
        if(waitForUser.isShowing()) {
            waitForUser.dismiss();
        }
        Toast.makeText(getApplicationContext(), "Użytkownik odrzucił propozycję rozmowy", Toast.LENGTH_LONG).show();
    }

    /**
     * Przejście do chatactivity
     * @param user użytkownik podłączony
     * @param secret sekret
     */
    private void startChatActivity(User user, byte[] secret) {
        ChatActivity_.intent(this).secret(secret).connectedUser(user).start();
    }

    /**
     * Obsługuje przychodzące żądanie połączenia
     * @param jsonUser użytkownik w JSON
     */
    public void connectionRequest(String jsonUser) {
        Log.d(LOG_TAG, "Connection request from "+jsonUser);
        try {
            User user = getUser(jsonUser);
            showNewConnectionDialog(user);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while parsing user json", e);
        }
    }

    /**
     * Wyświetla dialog przychodzącego połączenia
     * @param user użytkownik
     */
    private void showNewConnectionDialog(final User user) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.connectionRequest));
        builder.setIcon(android.R.drawable.sym_action_chat);
        builder.setPositiveButton(getString(R.string.acceptConnection), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                acceptConnection(user);
            }
        });
        builder.setNegativeButton(getString(R.string.rejectConnection), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                rejectConnection(user);
            }
        });
        builder.setMessage(user.getNick());
        builder.show();
    }

    /**
     * Zaakceptowanie połączenia
     * @param user użytkownik
     */
    private void acceptConnection(User user) {
        Log.d(LOG_TAG, "Accepted connection");
        sendAcceptConnection(user);
    }

    /**
     * Wysłanie info o zaakceptowaniu połączenia
     * @param user
     */
    @Background
    public void sendAcceptConnection(User user) {
        ConnectToUser request = new ConnectToUser();
        try {
            PublicKey key = diffieHellman.generateKeypair(user.getKey());
            request.setSocketId(user.getId());
            request.setKey(key.getEncoded());
            connection.acceptConnection(request);
            startChatActivity(user, diffieHellman.getSecret());
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
            Log.e(LOG_TAG, "Error while calculating DH", e);
        }
    }

    /**
     * Wysłanie info o odrzuceniu połączenia
     */
    @Background
    public void sendRejectConnection(User user) {
        ConnectToUser request = new ConnectToUser();
        request.setSocketId(user.getId());
        connection.rejectConnection(request);
    }

    /**
     * Odrzucenie połączenia
     * @param user
     */
    private void rejectConnection(User user) {
        Log.d(LOG_TAG, "Refused connection");
        sendRejectConnection(user);
    }
}
