package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.CartMenuDto;
import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.dto.TopMenusDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.IMenuService;
import com.example.PayoEat_BE.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
@Tag(name = "Menu Controller", description = "Endpoint for managing restaurant's menu")
public class MenuController {
    private final IMenuService menuService;
    private final IUserService userService;

    @PostMapping(value = "/add-menu", consumes = {"multipart/form-data"})
    @Operation(summary = "Add Menu in Restaurant", description = "API for adding menu in restaurant")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> addMenu(
            @RequestParam("menuName") String menuName,
            @RequestParam("menuDetail") String menuDetail,
            @RequestParam("menuPrice") double menuPrice,
            @RequestParam("isActive") boolean isActive,
            @RequestParam("menuImageFile") MultipartFile menuImageFile) {
        User user = userService.getAuthenticatedUser();
        AddMenuRequest request = new AddMenuRequest(menuName, menuDetail, menuPrice, isActive);
        UUID id = menuService.addMenu(request, menuImageFile, user.getId());
        return ResponseEntity.ok(new ApiResponse("Menu successfully added", id));
    }

    @PostMapping(value = "/edit-menu", consumes = {"multipart/form-data"})
    @Operation(summary = "Edit Menu in Restaurant", description = "API for edit menu in restaurant")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> editMenu(
            @RequestParam("menuCode") UUID menuCode,
            @RequestParam("menuName") String menuName,
            @RequestParam("menuDetail") String menuDetail,
            @RequestParam("menuPrice") double menuPrice,
            @RequestParam("isActive") boolean isActive,
            @RequestParam(value = "menuImageFile", required = false) MultipartFile menuImageFile) {
        User user = userService.getAuthenticatedUser();
        AddMenuRequest request = new AddMenuRequest(menuName, menuDetail, menuPrice, isActive);
        UUID id = menuService.editMenu(request, menuImageFile, user.getId(), menuCode);
        return ResponseEntity.ok(new ApiResponse("Menu successfully edited", id));
    }

    @PostMapping(value = "/delete")
    @Operation(summary = "Delete Menu in Restaurant", description = "API for delete menu in restaurant")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> editMenu(@RequestParam("menuCode") UUID menuCode) {
        User user = userService.getAuthenticatedUser();
        menuService.deleteMenu(menuCode, user.getId());
        return ResponseEntity.ok(new ApiResponse("Menu successfully deleted", null));
    }


    @PostMapping(value = "/edit-availability")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> editAvailability(@RequestParam UUID menuCode) {
        User user = userService.getAuthenticatedUser();
        menuService.editMenuAvailability(menuCode, user.getId());
        return ResponseEntity.ok(new ApiResponse("Menu availability updated", null));
    }

    @PostMapping(value = "/edit-all-availability")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> editAllAvailability(@RequestParam UUID restaurantId, @RequestParam boolean activate) {
        User user = userService.getAuthenticatedUser();
        menuService.editAllMenuAvailability(restaurantId, user.getId(), activate);
        return ResponseEntity.ok(new ApiResponse("All menu availability updated", null));
    }

    @GetMapping("/get-menu-by-code")
    @Operation(summary = "Show Menus by Menu Code", description = "API to display menus by providing menu code")
    public ResponseEntity<ApiResponse> getMenuByCode(@RequestParam UUID[] menuCodes) {
        CartMenuDto result = menuService.getMenuByCode(menuCodes);
        return ResponseEntity.ok(new ApiResponse("Result: ", result));
    }

    @GetMapping("/get-menus")
    @Operation(summary = "Show Menus by Restaurant ID", description = "API to display menus by providing restaurant ID")
    public ResponseEntity<ApiResponse> getActiveMenuByRestaurant(@RequestParam UUID restaurantId) {
        List<Menu> menus = menuService.getAllActiveMenu(restaurantId);
        List<MenuDto> convertedMenus = menuService.getConvertedMenus(menus);
        return ResponseEntity.ok(new ApiResponse("List of menus: ", convertedMenus));
    }

    @GetMapping("/get-menus-by-restaurant")
    @Operation(summary = "Show All Menus by Restaurant ID", description = "API to display all menus (active and inactive) by providing restaurant ID")
    public ResponseEntity<ApiResponse> getAllMenusOfRestaurant(@RequestParam UUID restaurantId) {
        List<Menu> menus = menuService.getMenusByRestaurantId(restaurantId);
        List<MenuDto> convertedMenus = menuService.getConvertedMenus(menus);
        return ResponseEntity.ok(new ApiResponse("List of menus: ", convertedMenus));
    }

    @GetMapping("/get-top-5-menus")
    @Operation(summary = "Get top 5 menus of restaurant", description = "API to display top 5 menus by restaurant id")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getTop5Menus(@RequestParam UUID restaurantId) {
        User user = userService.getAuthenticatedUser();
        List<TopMenusDto> topMenusDtos = menuService.getTop5Menu(restaurantId, user.getId());
        return ResponseEntity.ok(new ApiResponse("List of menus: ", topMenusDtos));
    }

    @GetMapping("/detail")
    @Operation(summary = "Get menu detail", description = "Get details of a menu")
    @PreAuthorize("hasAnyAuthority('RESTAURANT')")
    public ResponseEntity<ApiResponse> getMenuDetail(@RequestParam UUID menuCode) {
        User user = userService.getAuthenticatedUser();
        Menu result = menuService.getMenuDetail(menuCode, user.getId());
        return ResponseEntity.ok(new ApiResponse("Menu detail: ", result));
    }

}
