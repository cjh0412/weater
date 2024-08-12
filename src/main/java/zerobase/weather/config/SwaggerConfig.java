package zerobase.weather.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] paths = {"/read/**"}; // swagger에 보일 path만 설정
        String[] packagesToscan = {"zerobase.weather"};
        return GroupedOpenApi.builder()
                .group("springdoc-openapi")
//                .pathsToMatch(paths)
                .packagesToScan(packagesToscan)
                .build();

    }

    private Info apiInfo() {
        String description = "날씨 일기를 CRUD할 수 있는 백엔드 api입니다.";
        return new Info()
                .title("날씨 일기 프로젝트")
                .description(description)
                .version("2.0");
    }


}
