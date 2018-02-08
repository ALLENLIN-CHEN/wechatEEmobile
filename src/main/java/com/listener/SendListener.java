package com.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;

import com.util.PropertyUtil;

/**
 * @author phan at 2016年3月2日
 *
 */
@Component
public class SendListener implements  ApplicationListener<ContextRefreshedEvent> {
	private long DAY = 24*60*60*1000;
	//private long DAY = 60*1000;
	private Timer timer;
	private int sendHour ;
	private int sendMin ;//=Integer.parseInt(PropertyUtil.getProperty("sendMin"));
	@Autowired
	private SendTask sendTask;
	//private ServletContext context;
	

	
	public void resetSendTime(int hour,int min){
		this.sendHour = hour;
		this.sendMin = min;
		this.timer.cancel();
		Calendar calendar = Calendar.getInstance();
		int curHour =calendar.get(Calendar.HOUR_OF_DAY);
		int curMin = calendar.get(Calendar.MINUTE);
		long delay = 0;
		if(curHour>sendHour&&curMin>sendMin){
			calendar.add(Calendar.DAY_OF_MONTH, 1);	
		}
		calendar.set(Calendar.HOUR_OF_DAY, this.sendHour);
		calendar.set(Calendar.MINUTE,this.sendMin);
		Date execPoint = calendar.getTime();//确切的推送事件点
		delay = execPoint.getTime()-new Date().getTime();
		
		//定义定时器
		timer = new Timer("推送",true);

	    //启动推送任务,每天执行一次
		timer.schedule(this.sendTask,delay, DAY );

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event)
	{
		if(event.getApplicationContext().getParent() != null)
			return;//防止子容器多次调用
		this.sendHour = Integer.parseInt(PropertyUtil.getProperty("sendHour"));
		this.sendMin = Integer.parseInt(PropertyUtil.getProperty("sendMin"));
		Calendar calendar = Calendar.getInstance();
		int curHour =calendar.get(Calendar.HOUR_OF_DAY);
		int curMin = calendar.get(Calendar.MINUTE);
		long delay = 0;
		if(curHour>sendHour||(curHour==sendHour&&curMin>sendMin)){
			calendar.add(Calendar.DAY_OF_MONTH, 1);	
		}
		calendar.set(Calendar.HOUR_OF_DAY, this.sendHour);
		calendar.set(Calendar.MINUTE,this.sendMin);
		Date execPoint = calendar.getTime();//确切的推送事件点
		delay = execPoint.getTime()-new Date().getTime();
		
		//定义定时器
		timer = new Timer("推送",true);

	    //启动推送任务,每天执行一次
		timer.schedule(this.sendTask,delay, DAY );
//		timer.schedule(this.sendTask,0, DAY );
		//this.context.setAttribute("sendListener", this);
		
	}

//	@Override
//	public void setServletContext(ServletContext servletContext) {
//		this.context = servletContext;
//		
//	}

}
