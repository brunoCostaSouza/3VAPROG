package util;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class CustomFeedbackPanel extends FeedbackPanel{
	private static final long serialVersionUID = 1L;
	
	public CustomFeedbackPanel(String id) {
		super(id);
	}
	
	@Override
	protected String getCSSClass(FeedbackMessage message) {
		if(message.getLevel() == FeedbackMessage.SUCCESS ){
			return "alert alert-success";
		}else if(message.getLevel() == FeedbackMessage.ERROR){
			return "alert alert-danger";
		}else if(message.getLevel() == FeedbackMessage.INFO){
			return "alert alert-info";
		}else{
			return "alert alert-warning";
		}
	}
	
}
