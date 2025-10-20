package io.ourohead.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EditorViewController {

    /**
     * Forwards GET requests for the editor endpoint to the application's index resource.
     *
     * @return the view name {@code "forward:/ourohead/index.html"} which causes an internal forward to {@code /ourohead/index.html}
     */
    @GetMapping("/ourohead/editor")
    public String editorPage() {
        return "forward:/ourohead/index.html";
    }
}