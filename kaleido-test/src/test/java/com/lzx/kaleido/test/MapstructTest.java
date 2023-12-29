package com.lzx.kaleido.test;

import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateConfigEntity;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaModelConfigVO;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.infra.base.utils.PojoConvertUtil;
import com.lzx.kaleido.start.StartApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lwp
 * @date 2023-12-10
 **/
@Slf4j
@SpringBootTest(classes = StartApplication.class)
public class MapstructTest {
    
    @Test
    void beanToJsonTest() {
        final JavaModelConfigVO javaConfigVO = new JavaModelConfigVO();
        javaConfigVO.setSuperclass("BaseVO");
        javaConfigVO.setName("Demo.java");
        javaConfigVO.setCodePath("C:\\java");
        javaConfigVO.setSourceFolder("src/main/java");
        javaConfigVO.setPackageName("com.code4j.pojo.vo");
        final CodeGenerationTemplateConfigVO vo = new CodeGenerationTemplateConfigVO();
        vo.setTemplateParams(javaConfigVO);
        final CodeGenerationTemplateConfigEntity entity = PojoConvertUtil.vo2Entity(vo, CodeGenerationTemplateConfigEntity.class);
        System.out.println(entity.getTemplateParams());
        log.info("beanToJsonTest->{}", entity.getTemplateParams());
        Assertions.assertNotNull(entity.getTemplateParams());
    }
    
    /**
     *
     */
    @Test
    void jsonToBeanTest() {
        final String json = "{\"configName\":\"Model\",\"superclass\":\"BaseVO\",\"name\":\"Demo.java\",\"rootPath\":\"src/main/java\",\"codePath\":\"C:\\\\java\",\"packageName\":\"com.code4j.pojo.vo\"}";
        final CodeGenerationTemplateConfigEntity entity = new CodeGenerationTemplateConfigEntity();
        entity.setTemplateParams(json);
        final CodeGenerationTemplateConfigVO vo = PojoConvertUtil.entity2Vo(entity, CodeGenerationTemplateConfigVO.class);
        log.info("jsonToBeanTest->{}", JsonUtil.toJson(vo));
        Assertions.assertNotNull(vo);
    }
    
}
