module org.ssldev.api.sslapi {
	requires java.desktop;
	requires spring.web;
	requires spring.websocket;
	requires spring.core;
	requires spring.context;
	requires spring.beans;
	requires spring.boot;
	requires spring.boot.autoconfigure;
	requires org.ssldev.core;
	requires javafx.controls;
	requires javafx.fxml;
	
	exports org.ssldev.api.app;
	exports org.ssldev.api.messages;
	exports org.ssldev.api.web;
	exports org.ssldev.api.services;
}