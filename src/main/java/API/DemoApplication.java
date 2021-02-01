package API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import sample.Data;
import sample.HibernateAnnotationUtil;

@SpringBootApplication
@RestController

public class DemoApplication {

	public static void main(String[] args) {

		FulfillmentCenterContainer Warehouses=new FulfillmentCenterContainer();
		//HibernateAnnotationUtil HB=new HibernateAnnotationUtil();
		//Warehouses= HB.getHibernate();
		Warehouses=Data.getData();
		SpringApplication.run(DemoApplication.class, args);
	}

}
