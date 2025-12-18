package com.sonarshowcase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SPA Controller for client-side routing support.
 * 
 * Forwards all non-API, non-static requests to index.html
 * so that React Router can handle the routing.
 * 
 * @author SonarShowcase
 */
@Controller
public class SpaController {
    
    /**
     * Default constructor for SpaController.
     */
    public SpaController() {
    }

    /**
     * Forward all requests that don't match API endpoints or static resources
     * to the React SPA's index.html.
     * 
     * This enables client-side routing with React Router.
     * Routes like /users, /dashboard, etc. will be handled by React.
     *
     * @return Forward path to index.html
     */
    @GetMapping(value = {
        "/",
        "/{path:^(?!api|static|assets|favicon\\.ico).*$}",
        "/{path:^(?!api|static|assets|favicon\\.ico).*$}/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}

