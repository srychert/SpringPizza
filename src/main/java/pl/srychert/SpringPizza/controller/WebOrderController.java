package pl.srychert.SpringPizza.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.srychert.SpringPizza.service.OrderService;

@Controller
@RequestMapping("order")
public class WebOrderController {

    private final OrderService orderService;

    public WebOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/list")
    @Secured("ROLE_ADMIN")
    String getAll(Model model) {
        model.addAttribute("orderList", orderService.getAll());
        return "order-list";
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("@authComponent.isOrderOwner(#orderId) or hasRole('ROLE_ADMIN')")
    String get(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", orderService.get(orderId));
        return "order-details";
    }

    @GetMapping("/delete/{orderId}")
    @PreAuthorize("@authComponent.isOrderOwner(#orderId) or hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable Long orderId, Model model) {
        orderService.delete(orderId);

        return "redirect:/order/list";
    }

}
