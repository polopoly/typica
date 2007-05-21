
import java.util.List;

import org.apache.log4j.Logger;

import com.xerox.amazonws.sqs.QueueService;
import com.xerox.amazonws.sqs.Message;
import com.xerox.amazonws.sqs.MessageQueue;
import com.xerox.amazonws.tools.LoggingConfigurator;

public class TestVisibility {
    private static Logger log = LoggingConfigurator.configureLogging(TestVisibility.class);

	public static void main(String [] args) throws Exception {
        final String AWSAccessKeyId = "[AWS Access Id]";
        final String SecretAccessKey = "[AWS Secret Key]";

		QueueService qs = new QueueService(AWSAccessKeyId, SecretAccessKey, false, "localhost");
		MessageQueue mq = qs.getOrCreateMessageQueue(args[0]);
		int timeout = mq.getVisibilityTimeout();
		log.debug("Queue timeout = "+timeout);
		mq.sendMessage("Testing 1, 2, 3");
		try { Thread.sleep(5000); } catch (InterruptedException ex) {}
		Message msg = mq.receiveMessage();
		log.debug("Message = "+msg.getMessageBody());
		int i=0;
		while ((msg = mq.receiveMessage()) == null) {
			log.debug(".");
			try { Thread.sleep(1000); } catch (InterruptedException ex) {}
			i++;
		}
		log.debug("Message was invisible for "+i+" seconds");
		mq.deleteMessage(msg.getMessageId());

		// test queue visibility
		log.debug("setting timeout to 10 seconds.");
		mq.setVisibilityTimeout(10);
		mq.sendMessage("Testing 1, 2, 3");
		try { Thread.sleep(5000); } catch (InterruptedException ex) {}
		msg = mq.receiveMessage();
		log.debug("Message = "+msg.getMessageBody());
		i=0;
		while ((msg = mq.receiveMessage()) == null) {
			log.debug(".");
			try { Thread.sleep(1000); } catch (InterruptedException ex) {}
			i++;
		}
		log.debug("Message was invisible for "+i+" seconds");
		mq.deleteMessage(msg.getMessageId());

		// test receive visibility
		mq.sendMessage("Testing 1, 2, 3");
		try { Thread.sleep(5000); } catch (InterruptedException ex) {}
		msg = mq.receiveMessage(30);
		log.debug("Message = "+msg.getMessageBody());
		i=0;
		while ((msg = mq.receiveMessage()) == null) {
			log.debug(".");
			try { Thread.sleep(1000); } catch (InterruptedException ex) {}
			i++;
		}
		log.debug("Message was invisible for "+i+" seconds");
		mq.deleteMessage(msg.getMessageId());
		// reset queue timeout
		mq.setVisibilityTimeout(timeout);
	}
}
