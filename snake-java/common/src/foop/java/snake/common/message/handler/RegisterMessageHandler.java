package foop.java.snake.common.message.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.RegisterAckMessage;
import foop.java.snake.common.message.RegisterErrorMessage;
import foop.java.snake.common.message.RegisterMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.TCPClient;
import foop.java.snake.common.tcp.TCPClientRegistry;

/**
 * Handles messages sent by the client to the server to register new players.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterMessageHandler implements MessageHandlerInterface
{
    protected PlayerRegistry playerRegistry;
    protected TCPClientRegistry clientRegistry;

    public RegisterMessageHandler(PlayerRegistry playerRegistry, TCPClientRegistry clientRegistry)
    {
        this.playerRegistry = playerRegistry;
        this.clientRegistry = clientRegistry;
    }

    /**
     * Handles a message sent by the client to the server to register a new player.
     *
     * @param  rawMessage
     * @param  address
     * @throws NoMessageHandlerFoundException when no handler can be found
     */
    public void handle(MessageInterface rawMessage, SocketAddress address)
        throws NoMessageHandlerFoundException
    {
        if (rawMessage.getType() != RegisterMessage.TYPE) {
            throw new NoMessageHandlerFoundException("This is not a RegisterMessage.");
        }
        RegisterMessage message = (RegisterMessage)rawMessage;

        System.out.println("RegisterMessageHandler: "+message.getPlayerName() + " wants to register at the server.");

        MessageInterface response;

        InetSocketAddress newAddress = new InetSocketAddress(((InetSocketAddress)address).getHostName(), message.getPort());

        if (playerRegistry.hasPlayerName(message.getPlayerName())) {
            response = new RegisterErrorMessage("The name \"" + message.getPlayerName() + "\" is already taken. Please choose another name.");
            System.out.println("RegisterMessageHandler: Username \"" + message.getPlayerName() + "\" already exists.");
            return;
        } else {
        	Player p = new Player(message.getPlayerName());
            p.setAddress(newAddress);
            playerRegistry.addPlayer(p);
            System.out.println("RegisterMessageHandler: Registered " + message.getPlayerName());
            response = new RegisterAckMessage(p.getId());
        }

        System.out.println("RegisterMessageHandler: address="+newAddress.getHostName()+";port="+newAddress.getPort());

        try {
            TCPClient client = clientRegistry.getClient(newAddress);
            client.sendMessage(response);
//            testMessages(client);
        } catch (Exception ex) {
            System.out.println("RegisterMessageHandler: Couldn\'t send response to \"" + ((InetSocketAddress)newAddress).getHostName()+":"+((InetSocketAddress)newAddress).getPort() + "\".");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Dummy method to send and test board and PrioChanged
     * @param client
     * @throws Exception
     */
    private void testMessages(TCPClient client) throws Exception {
    	MessageInterface response;

		// Test: send board and prio-Message just for test and fun
		Player p1 = new Player("Player 1");
		p1.setId(1);
		Player p2 = new Player("Player 2");
		p2.setId(2);
		Player p3 = new Player("Player 3");
		p3.setId(3);
		Player p4 = new Player("Player 4");
		p4.setId(4);
		Player p5 = new Player("Player 5");
		p5.setId(5);
		List<Player> pl = new ArrayList<Player>();
		pl.add(p1);
		pl.add(p2);
		pl.add(p3);
		pl.add(p4);
		pl.add(p5);

	/*	response = new PrioChangeMessage(pl);

        try {
            System.out.println("RegisterMessageHandler: Sending priorities message");
            client.sendMessage(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/

		Board board = new Board(30, 30);
		Byte[][] b = new Byte[][]{
			{0, 0, (byte)0x20, (byte)0x10, (byte)0x10, 0, 0, 0, 0},
			{0, 0, 0, 0, (byte)0x10, 0, 0, 0, 0},
			{0, 0, 0, 0, (byte)0x10, 0, 0, 0, 0},
			{0, 0, 0, 0, (byte)0x10, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, (byte)0x51, 0},
			{0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
			{0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
			{0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
			{0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0}
		};
		board.setBoard(b);

        response = new BoardMessage(board);
        try {
		  System.out.println("RegisterMessageHandler: Sending board message");
	        client.sendMessage(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
