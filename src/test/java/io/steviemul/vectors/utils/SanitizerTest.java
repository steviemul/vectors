package io.steviemul.vectors.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SanitizerTest {

  @Test
  void testBasic() {

    String input = "{{#formatting}}This is a sanitizer test{{formatting}}";

    input = Sanitizer.sanitize(input);

    assertNotNull(input);
  }

}