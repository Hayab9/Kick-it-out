package update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

public class Server {

    ServerSocket serverSocket ;
    public static ArrayList<prosseses> players = new ArrayList<>();
    int playernum = 0;

    public Server() {
        try {
            serverSocket = new ServerSocket(1234, 1, InetAddress.getByName("0.0.0.0"));
            System.out.println("server is running and waiting for connections on port 1234");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected from : " + clientSocket.getInetAddress().getHostAddress());
                prosseses process = new prosseses(this, clientSocket);
                players.add(process);

                process.start();
            }
        } catch (IOException ex) {
                System.err.println("Error while accepting client: " + ex.getMessage());

        }
    }

    public void sendplayers() {
        String data = "";
        for (prosseses item : Server.players) {
            data += item.playername + " Score " + "Connected #";
        }
        for (prosseses item : Server.players) {
            item.output.println(data);
            item.output.flush();
        }
    }

    public void removePlayer(prosseses player) {
        try {
            // Remove the player from the list
            players.remove(player);
            System.out.println("Player " + player.playername + " has disconnected.");

            // Update other clients
            sendplayers();

            // Close the player's socket and streams
            player.client.close();
            player.input.close();
            player.output.close();

        } catch (IOException ex) {
            System.err.println("Error while removing player: " + ex.getMessage());
        }
    }
   
    public void StartGame(int quest, int count) throws InterruptedException {
        PrintAllPlayers();
        if (count == 2) {
            for (prosseses item : Server.players) {
                if (item.Played) {
            item.startTimer();}}
            for (prosseses item : Server.players) {
                if (item.Played) {
                    switch (quest) {
                        case 1: {
                            for (prosseses x : Server.players) {
                                if (x.Played) {
                                    switch (x.quest) {
                                        case 1: x.output.println("Question im in my own home") ;break;
                                        case 2: x.output.println("Question I am just a little tired today");break;
                                        case 3 : x.output.println("Question He absolutely loves that movie.");break;
                                        case 4 : x.output.println("Question They ran quickly to catch the bus.");break;
                                        case 5 : x.output.println("Question She gave me a really nice gift.");break;
                                        default : {
                                        }
                                    }
                                }
                            }
                        }
                        case 2 : {
                            for (prosseses x : Server.players) {
                                if (x.Played) {
                                    switch (item.quest) {
                                        case 2: item.output.println("Question I am just a little tired today");break;
                                        case 1: item.output.println("Question im in my own home");break;
                                        case 3 : item.output.println("Question He absolutely loves that movie.");break;
                                        case 4: item.output.println("Question They ran quickly to catch the bus.");break;
                                        case 5 : item.output.println("Question She gave me a really nice gift.");break;
                                         default : {
                                        }
                                    }
                                }
                            }
                        }
                        case 3 : {
                            for (prosseses x : Server.players) {
                                if (x.Played) {
                                    switch (item.quest) {
                                        case 3: item.output.println("Question He absolutely loves that movie.");break;
                                        case 2 : item.output.println("Question I am just a little tired today");break;
                                        case 1: item.output.println("Question im in my own home");break;
                                        case 4 :item.output.println("Question They ran quickly to catch the bus.");break;
                                        case 5 :item.output.println("Question She gave me a really nice gift.");break;
                                         default : {
                                        }

                                    }
                                }
                            }
                        }
                        case 4 : {
                            for (prosseses x : Server.players) {
                                if (x.Played) {
                                    switch (item.quest) {
                                        case 4 : item.output.println("Question They ran quickly to catch the bus.");break;
                                        case 2: item.output.println("Question I am just a little tired today");break;
                                        case 1: item.output.println("Question im in my own home");break;
                                        case 3 : item.output.println("Question He absolutely loves that movie.");break;
                                        case 5 : item.output.println("Question She gave me a really nice gift.");break;
                                         default : {
                                        }

                                    }
                                }
                            }
                        }
                        case 5 :{
                            for (prosseses x : Server.players) {
                                if (x.Played) {
                                    switch (item.quest) {
                                        case 5 : item.output.println("Question She gave me a really nice gift.");break;
                                        case 2 : item.output.println("Question I am just a little tired today");break;
                                        case 1: item.output.println("Question im in my own home");break;
                                        case 3 : item.output.println("Question He absolutely loves that movie.");break;
                                        case 4 : item.output.println("Question They ran quickly to catch the bus.");break;
                                        default : {
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

            }

        } else {
            for (prosseses item : Server.players) {
                if (item.Played) {
                    item.output.println("Waiting for other players");
                    item.output.flush();
                }
            }
        }
    }
    public void PrintAllPlayers() {
        String data = "";
        
        for (prosseses item : Server.players) {
            if (item.Played) {
                data += item.playername + " Score= " + item.score + "Playing #";
            }
            data += item.playername + " Score"  + "Connected #";
        }
        
        for (prosseses item : Server.players) {
            item.output.println(data);
            item.output.flush();
        }
    }

    public void PrintPlayRequest(String username) {
        for (prosseses items : players) {
            if (items.Played) {
                items.output.println("Notify:" + username + " Player request to play");
                items.output.flush();
            }
        }
    }

    public void Winner(String playername) {
        String data = "";
        
        for (prosseses item : Server.players) {
            
            item.output.println("stop the winner is: " + playername);
        }
    }

    public void stopGame() {
        boolean nostop = true;
        
        for (prosseses x : Server.players) {
            if (x.score == 5) {
                nostop = false;
                
                break;
            }
        }
        
        if (nostop) {
            for (prosseses item : Server.players) {
                item.output.println("stop Game Over No Winner.");
            }
        }
    }

    public String ranking() {
        String rank = "";
        String secondPlayer = "";
        String thirdPlayer = "";
        int secondScore = 0;
        int thirdScore = 0;
        
        for (prosseses item : Server.players) {
            if (item.Played) {
                if (item.score == 5) {
                    rank += "Rank 1: " + item.playername + " score: "+item.score+"#";
                } else if (item.score >= 0 && item.score <= 4) {
                    if (item.score > secondScore) {
                        thirdScore = secondScore;
                        thirdPlayer += secondPlayer;
                        secondScore = item.score;
                        secondPlayer += item.playername;
                    } else if (item.score < secondScore) {
                        thirdScore = item.score;
                        thirdPlayer += item.playername;
                    } else if (item.score >= secondScore) {
                        thirdScore = secondScore;
                        thirdPlayer += secondPlayer;
                        secondScore = item.score;
                        secondPlayer += item.playername;
                    }
                }
            }
        }
        
        rank += "Rank 2: " + secondPlayer +" score: "+secondScore+ "#";
        
        if (playernum == 3) {
            rank += "Rank 3 : " + thirdPlayer +" score: "+thirdScore+  "#";
        }
        
        return rank;
    }

    public void rank() {
        String text = ranking();
        
        for (prosseses item : Server.players) {
            if (item.Played) {
                item.output.println("Ranking: #" + text);
            }
        }
    }
    
     public static void main(String[] args) {
        new Server();

    }
     
     
     
public void stopGameIfOnePlayerRemains(String s) {
    if (playernum == 1){
    for (prosseses item : Server.players) {
     
        item.output.println("stop No Game Over.");
        
}
}
    if (playernum >4){
         for (prosseses item : Server.players)
             if(item.playername.equals(s))
             item.output.println("stop No");
             }
    
}
    
}

