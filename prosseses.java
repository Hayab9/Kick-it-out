package update;

import update.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

public class prosseses extends Thread {
 boolean f = true;
    Server server;
    Socket client;
    String playername;
    BufferedReader input;
    PrintWriter output;
    String recievedData;
    boolean Played = false;
    //boolean Connected=false;
    int score = 0;
    int quest = 1;

    prosseses(Server server, Socket clientSocket) {
        try {
            this.server = server;
            client = clientSocket;
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(prosseses.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                while (!(recievedData = input.readLine()).equals(null) && input != null) {
                    if (!recievedData.equals(null)) {
                        if (recievedData.contains("Name:")) {
                            playername = recievedData.replace("Name:", "");
                            server.sendplayers();
                        } else if (recievedData.contains("Play:")) {
                            this.Played = true;
                            playername = recievedData.replace("Play:", "");
                            server.PrintPlayRequest(playername);
                            server.PrintAllPlayers();
                            server.playernum++;

                            if (server.playernum == 1) {
                                server.StartGame(quest, server.playernum);
                            }
                            if (server.playernum == 2) {
                                for (prosseses item : Server.players) {
                                    item.output.println("Waiting for 30s to start the game");
                                }
                                Thread.sleep(30000);  // the waiting timer before game start
                                
                                 if(server.playernum == 2){
                                       for (prosseses item : Server.players) {
                                           if(item.Played){
                                              item.server.StartGame(quest, 2);}
                                           }
                                       }else{
                                 
                                    for (prosseses item : Server.players) {
                                           if(item.Played){
                                              
                                              item.server.StartGame(quest, 2);}
                                 }}
                                
                          
                            }

                        } else if (recievedData.contains("Answer")) {
                            updatescore(recievedData);
                            server.PrintAllPlayers();
                        } else if (recievedData.contains("done")) {
                            Played = false;
                            server.PrintAllPlayers();
                            server.playernum = 0;
                            
                        } else if (recievedData.contains("Finishgame")) {
                            String s =recievedData.replace("Finishgame", "");
                            Played = false;
                            server.PrintAllPlayers();
                            server.playernum --;
                            server.stopGameIfOnePlayerRemains(s);}
                        else if (recievedData.contains("Disconnect")) {
                            server.playernum --;
                            server.removePlayer(this);
                            // disconnectPlayer();
                            //   input.close();
                            //  output.close();
                            // client.close();
                            server.players.remove(this);
                            server.PrintAllPlayers();
                            break; // Exit the loop and end the thread
                        }
                        
                        
                    } else {
                        Printplayers();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(prosseses.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(prosseses.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                            
    }
    private void disconnectPlayer() {
    try {
        for (prosseses item : Server.players) {
            if (item.client == client) {
                Server.players.remove(item);
                break;
            }
        }
        client.close();
        input.close();
        output.close();
        server.sendplayers(); // Inform other players about the disconnection
    } catch (IOException ex) {
        Logger.getLogger(prosseses.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    private void updatescore(String userAnswer) throws InterruptedException {
        String firstRank = "";
        String[] data = userAnswer.split(" ");
        for (prosseses item : Server.players) {
            if (item.playername.equals(data[1])) {
                boolean correct = true;
                                for (int i = 0; i < data[2].length(); i++) {
                            if (!(data[2].equals("own") || data[2].equals("really") || data[2].equals("quickly")
                             || data[2].equals("just") || data[2].equals("absolutely"))) {
                        correct = false;
                    }
                }

                if (correct) {
                    item.score++;
                    if (item.score == 5) {
                        server.Winner(item.playername);
                        server.rank();
                        break;
                    }
                    item.quest++;
                    server.StartGame(quest, 2);
                }
            }
        }
    }

    private void Printplayers() {
        String data = "";
        for (prosseses item : Server.players) {
            if (item.Played) {
                data += item.playername + " Score= " + item.score + " Playing #";
            } else {
                data += item.playername + " Score= " + item.score + " Connected #";
            }
        }
        for (prosseses item : Server.players) {
            output.println(data);
            output.flush();
        }
    }
 public void startTimer() {
    final int[] timeRemaining = {30}; 
   

    // Display initial time remaining (optional)
    System.out.println("Time remaining: " + timeRemaining[0] + " seconds");

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            timeRemaining[0]--;

            // Display updated time remaining (optional)
            System.out.println("Time remaining: " + timeRemaining[0] + " seconds");

            // Check if time has run out
            if (timeRemaining[0] <= 0) {
                timer.cancel(); // Stop the timer
                System.out.println("Time's up! Game over.");

                // Check game conditions and stop the game
                for (prosseses item : Server.players) {
                    if (item.score == 5) {
                        f = false;
                    }
                }
                if (server.playernum == 1) {
                    f = false;
                }
                if (f) {
                    server.stopGame();
                }
            }                    
        }
    }, 0, 1000);
}
}
    

