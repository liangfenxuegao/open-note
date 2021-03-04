package xuegao.practice.openNote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching//开启基于注解的缓存机制
@EnableTransactionManagement//开启事务支持，配合@Transactional使用。
@EnableScheduling//启用Scheduling注解，才能使用@Scheduled。
@EnableAsync//启用Async注解，才能使用@Async。
@SpringBootApplication
public class OpenNoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenNoteApplication.class, args);
	}

}
