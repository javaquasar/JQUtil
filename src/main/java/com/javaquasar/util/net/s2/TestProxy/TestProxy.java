/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javaquasar.util.net.s2.TestProxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.impl.ProxyToServerConnection;

/**
 *
 * @author artur
 */
public class TestProxy {

    public static void main(String[] args) {
//        System.setProperty("javax.net.debug", "ssl");
//        System.getProperties().put("http.proxyHost", "someProxyURL");
//        System.getProperties().put("http.proxyPort", "someProxyPort");
        HttpProxyServer server
                = DefaultHttpProxyServer.bootstrap()
                        .withPort(10000)
                        .withFiltersSource(new HttpFiltersSourceAdapter() {
                            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                                return new HttpFiltersAdapter(originalRequest) {
                                    
                                    @Override
                                    public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {
                                        for(String s : serverCtx.pipeline().names()) {
                                            System.out.print(s + " = ");
//                                            if(serverCtx.pipeline().get(s) instanceof ProxyToServerConnection) {
//                                                ProxyToServerConnection httpRequestEncoder = (ProxyToServerConnection) serverCtx.pipeline().get(s);
//                                                System.out.println("HandshakeSession = " + httpRequestEncoder.getSslEngine().getHandshakeSession());
//                                                System.out.println("HandshakeStatus = " + httpRequestEncoder.getSslEngine().getHandshakeStatus());
//                                                System.out.println("PeerPort = " + httpRequestEncoder.getSslEngine().getPeerPort());
//                                                System.out.println("EndpointIdentificationAlgorithm = " + httpRequestEncoder.getSslEngine().getSSLParameters().getEndpointIdentificationAlgorithm());
//                                            }
                                        }
                                        
                                        super.proxyToServerConnectionSucceeded(serverCtx); //To change body of generated methods, choose Tools | Templates.
                                    }

                                    @Override
                                    public void proxyToServerResolutionSucceeded(String serverHostAndPort, InetSocketAddress resolvedRemoteAddress) {
                                        System.out.println("HostAndPort = " + serverHostAndPort.toString());
                                        System.out.println("resolvedRemoteAddress = " + resolvedRemoteAddress.getAddress());
                                        
                                        super.proxyToServerResolutionSucceeded(serverHostAndPort, resolvedRemoteAddress); //To change body of generated methods, choose Tools | Templates.
                                    }

                                    @Override
                                    public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
                                        return super.proxyToServerResolutionStarted(resolvingServerHostAndPort); //To change body of generated methods, choose Tools | Templates.
                                    }
                                    
                                    
                                    
                                    @Override
                                    public HttpObject proxyToClientResponse(HttpObject httpObject) {
                                        return super.proxyToClientResponse(httpObject); //To change body of generated methods, choose Tools | Templates.
                                    }                                   

                                    @Override
                                    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                        if (httpObject instanceof HttpRequest) {
                                            HttpRequest httpRequest = (HttpRequest) httpObject;
                                            System.out.println(httpRequest.getUri());
                                        }
                                        if (httpObject instanceof FullHttpRequest) {
                                            FullHttpRequest httpRequest = (FullHttpRequest) httpObject;
//                                            httpRequest.headers().remove("Host");
//                                            httpRequest.headers().add("Host", "myserver.com:8080");
                                            System.out.println("ProtocolVersion = " + httpRequest.getProtocolVersion());
                                            System.out.println("Method = " + httpRequest.getMethod());
                                            System.out.println("Headers = " + httpRequest.headers());
                                            System.out.println("DecoderResult = " + httpRequest.getDecoderResult().isSuccess());
                                        }
                                        return null;
                                    }

                                    @Override
                                    public HttpObject serverToProxyResponse(HttpObject httpObject) {
                                        if(httpObject instanceof HttpResponse) {
                                            HttpResponse httpResponse = (HttpResponse) httpObject;
                                            System.out.println("ProtocolVersion = " + httpResponse.getProtocolVersion());
                                            System.out.println("Status = " + httpResponse.getStatus());
                                            System.out.println("DecoderResult = " + httpResponse.getDecoderResult().isSuccess());
                                            for(String s : httpResponse.headers().names()) {
                                                System.out.print(s + " = ");
                                                System.out.println(httpResponse.headers().get(s));
                                            }
                                        }
                                        
                                        // TODO: implement your filtering here
                                        return httpObject;
                                    }
                                };
                            }
                        })
                        
                        .start();
    }

    public static class AnswerRequestFilter extends HttpFiltersAdapter {

        private final String answer;

        public AnswerRequestFilter(HttpRequest originalRequest, String answer) {
            super(originalRequest, null);
            this.answer = answer;
        }

        @Override
        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.wrappedBuffer(answer.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(TestProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
            HttpHeaders.setContentLength(response, buffer.readableBytes());
            HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, "text/html");
            return response;
        }

        @Override
        public HttpObject serverToProxyResponse(HttpObject httpObject) {
            System.out.println("123");
            // TODO: implement your filtering here
            return httpObject;
        }

    }
}
