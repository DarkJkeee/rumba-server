package com.company.rumba.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

public class PathProvider {
    public static String getCurrentPath() {
        UriComponents uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().build();
        return uriComponents.getPath();
    }
}
