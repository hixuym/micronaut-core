/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.inject.configproperties

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.ApplicationContext
import io.micronaut.context.DefaultApplicationContext
import io.micronaut.context.env.PropertySource
import io.micronaut.context.exceptions.BeanInstantiationException
import spock.lang.Specification

import javax.validation.Validation
import javax.validation.constraints.NotNull
import org.hibernate.validator.constraints.NotBlank
/**
 * Created by graemerocher on 15/06/2017.
 */
class ValidatedConfigurationSpec extends Specification {


    void "test validated config with invalid config"() {

        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.registerSingleton(
                Validation.buildDefaultValidatorFactory()
        )
        applicationContext.start()

        when:
        ValidatedConfig config = applicationContext.getBean(ValidatedConfig)

        then:
        def e = thrown(BeanInstantiationException)
        e.message.contains('url - must not be null')
        e.message.contains('name - may not be empty')
    }

    void "test validated config with valid config"() {

        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.environment.addPropertySource(PropertySource.of(
                'test',
                ['foo.bar.url':'http://localhost',
                'foo.bar.name':'test']
        ))

        applicationContext.registerSingleton(
                Validation.buildDefaultValidatorFactory()
        )

        applicationContext.start()

        when:
        ValidatedConfig config = applicationContext.getBean(ValidatedConfig)

        then:
        config != null
        config.url == new URL("http://localhost")
        config.name == 'test'

    }

    @ConfigurationProperties('foo.bar')
    static class ValidatedConfig {
        @NotNull
        URL url

        @NotBlank
        protected String name
    }
}
