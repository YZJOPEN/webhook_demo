package spring_boot;

/**
 * erp产品线用来接收webhook回调示例
 */
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class ReceiveEvent {
	
	//这里写成自己企业的token
	@Value("${erp.eidToken}")
	private String erpToken ;
	
	
	@RequestMapping("/webhookEventErp")
	public String webhookEventErp(@RequestHeader Map<String, String> header, @RequestParam(required=false) String eid,@RequestParam(required=false) String eventType,
	@RequestParam(required=false) String eventId,@RequestParam(required=false) String createTime) {
		String contentBody = "eid=" + eid+ "&eventType=" + eventType + "&eventId=" + eventId+"&createTime="+createTime;
		System.out.println(contentBody);
		Map<String,String> paramsMap = new TreeMap<String,String>();
		paramsMap.put("eid", eid);
		paramsMap.put("eventType", eventType);
		paramsMap.put("eventId", eventId);
		paramsMap.put("createTime", createTime);
		contentBody = mapToString(paramsMap);
		if(WebHookUtil.checkAuth(erpToken, contentBody, header)){
			System.out.println("接收到一个合法推送，内容为： "+contentBody);
			//处理推送的逻辑写在这里,异步处理，服务端2S超时
			//.....
		}else{
			System.out.println("接收到一个非法推送");
			return "not ok";
		}
		
		return "ok";
	}
	
	// 按key字段顺序排序，组装k1=v1&k2=v2形式
	private String mapToString(Map<String, String> map) {
	    StringBuilder sb = new StringBuilder();
	    Set<String> keys = map.keySet();
	    for (String key : keys) {
	        sb.append(key).append("=").append(map.get(key)).append("&");
	    }
	    if (sb.length() > 0) {
	        return sb.substring(0, sb.length() - 1);
	    } else {
	        return sb.toString();
	    }
	}

	public static void main(String[] args) {
		SpringApplication.run(ReceiveEvent.class, args);
	}
}
