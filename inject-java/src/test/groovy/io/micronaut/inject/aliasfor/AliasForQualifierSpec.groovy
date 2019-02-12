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
package io.micronaut.inject.aliasfor

import io.micronaut.inject.AbstractTypeElementSpec
import io.micronaut.inject.BeanDefinition
import spock.lang.Specification

import javax.inject.Named
import javax.inject.Qualifier
import java.util.function.Function

class AliasForQualifierSpec extends AbstractTypeElementSpec {


    void "test that when an alias is created for a named qualifier the stereotypes are correct"() {
        given:
        BeanDefinition definition = buildBeanDefinition('test.Test$MyFunc','''\
package test;

import io.micronaut.inject.aliasfor.*;
import io.micronaut.inject.annotation.*;
import io.micronaut.context.annotation.*;

@Factory
class Test {

    @TestAnnotation("foo")
    java.util.function.Function<String, Integer> myFunc() {
        return (str) -> 10;
    }
}

''')
        expect:
        definition != null
        definition.getAnnotationTypeByStereotype(Qualifier).isPresent()
        definition.getAnnotationTypeByStereotype(Qualifier).get() == TestAnnotation
        definition.getValue(Named, String).get() == 'foo'
    }
}
