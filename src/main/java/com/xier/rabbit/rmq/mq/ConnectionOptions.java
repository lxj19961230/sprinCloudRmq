package com.xier.rabbit.rmq.mq;

/*
 * rabbitMq链接信息
 */
public class ConnectionOptions {
     private String host="127.0.0.1";  //服务器ip
     private int port=-1; //服务器端口
     private String userName="";//用户名
     private String password = "";//密码
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
