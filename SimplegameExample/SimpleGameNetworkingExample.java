package kr.cbnu.lesson9;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.*;

public class SimpleGameNetworkingExample {
    private final List<UUID> map = new ArrayList<>();
    private final Map<UUID, Integer> score = new HashMap<>();
    private final Game game = new Game();

    private final String host;
    private final int port;
    private Channel channel;

    public SimpleGameNetworkingExample(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost"; // 호스트 주소 입력
        int port = 8888; // 포트 번호 입력

        SimpleGameNetworkingExample client = new SimpleGameNetworkingExample(host, port);
        client.start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new GameClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();

            // 클라이언트 입력 대기
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    break;
                }
                sendPacket(input);
            }

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendPacket(String data) {
        channel.writeAndFlush(data);
    }

    class GameClientHandler extends io.netty.channel.SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
            System.out.println(msg);
        }
    }

    private class Game {
        private static final Random random = new Random();
        private int[] playerValues = new int[]{random.nextInt(101), random.nextInt(101)};
        private boolean isFirstPlayerTurn = false;

        public Game.TurnResult doTurn(boolean isFirstPlayerTurn, int value) {
            if (this.isFirstPlayerTurn != isFirstPlayerTurn) {
                return Game.TurnResult.ILLEGAL_TURN;
            }
            this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
            return checkResult(value, playerValues[isFirstPlayerTurn ? 0 : 1]);
        }

        private Game.TurnResult checkResult(int value, int answer) {
            if (value == answer) {
                return Game.TurnResult.WIN;
            }
            if (value < answer) {
                return Game.TurnResult.DOWN;
            }
            return Game.TurnResult.UP;
        }
    }

    enum TurnResult {
        UP, DOWN, WIN, ILLEGAL_TURN
    }
}