package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
	public static void main(String[] args) {
		SpringApplication.run(EchoApplication.class, args);
	}

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		System.out.println("event: " + event);
		/*
		 * if (event.getMessage().getText() == "Hi" ||
		 * event.getMessage().getText().equals("Hi")) { return new
		 * TextMessage("Hello, How are you?"); } else { return new
		 * TextMessage(event.getMessage().getText()); }
		 */
		AIConfiguration configuration = new AIConfiguration("4082b96e0b0d4665938c945ba58c2ef5");

		AIDataService dataService = new AIDataService(configuration);

		try {
			AIRequest request = new AIRequest(event.getMessage().getText());

			AIResponse response = dataService.request(request);

			if (response.getStatus().getCode() == 200) {
				System.out.println(response.getResult().getFulfillment().getDisplayText());
			} else {
				System.err.println(response.getStatus().getErrorDetails());
			}

			if (response.getResult().getAction() == "weather") {
				System.out.println(event.getMessage().getText() + ":weather");
				return new TextMessage(event.getMessage().getText() + ":weather");
			} else {
				System.err.println(event.getMessage().getText() + ":NOT-weather");
				return new TextMessage(event.getMessage().getText() + ":NOT-weather");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}
}
