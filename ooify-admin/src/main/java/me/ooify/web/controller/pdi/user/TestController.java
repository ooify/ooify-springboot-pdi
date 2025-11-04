package me.ooify.web.controller.pdi.user;

import me.ooify.common.core.domain.AjaxResult;
import me.ooify.pdi.service.tool.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class TestController {
    @Autowired
    MessagingService messagingService;

    @GetMapping("/test")
    public AjaxResult test(String msg) {
        messagingService.sendPdiMessage(msg);
        return AjaxResult.success();
    }
}
