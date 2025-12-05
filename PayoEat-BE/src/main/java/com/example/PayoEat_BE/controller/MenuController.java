package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.CartMenuDto;
import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.dto.TopMenusDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.request.menu.UpdateMenuRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.menu.IMenuService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
@Tag(name = "Menu Controller", description = "Endpoint for managing restaurant's menu")
public class MenuController {
    private final IMenuService menuService;
    private final IUserService userService;

    @PostMapping(value = "/add-menu", consumes = {"multipart/form-data"})
    @Operation(summary = "Add Menu in Restaurant", description = "API for adding menu in restaurant")
    public ResponseEntity<ApiResponse> addMenu(
            @RequestParam("menuName") String menuName,
            @RequestParam("menuDetail") String menuDetail,
            @RequestParam("menuPrice") double menuPrice,
            @RequestParam("isActive") boolean isActive,
            @RequestParam("menuImageFile") MultipartFile menuImageFile) {
        try {
            User user = userService.getAuthenticatedUser();
            AddMenuRequest request = new AddMenuRequest(menuName, menuDetail, menuPrice, isActive);
            UUID id = menuService.addMenu(request, menuImageFile, user.getId());
            return ResponseEntity.ok(new ApiResponse("Menu successfully added", id));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @PostMapping(value = "/edit-menu", consumes = {"multipart/form-data"})
    @Operation(summary = "Edit Menu in Restaurant", description = "API for edit menu in restaurant")
    public ResponseEntity<ApiResponse> editMenu(
            @RequestParam("menuCode") UUID menuCode,
            @RequestParam("menuName") String menuName,
            @RequestParam("menuDetail") String menuDetail,
            @RequestParam("menuPrice") double menuPrice,
            @RequestParam("isActive") boolean isActive,
            @RequestParam(value = "menuImageFile", required = false) MultipartFile menuImageFile) {
        try {
            User user = userService.getAuthenticatedUser();
            AddMenuRequest request = new AddMenuRequest(menuName, menuDetail, menuPrice, isActive);
            UUID id = menuService.editMenu(request, menuImageFile, user.getId(), menuCode);
            return ResponseEntity.ok(new ApiResponse("Menu successfully added", id));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @PostMapping(value = "/preview-upload", consumes = {"multipart/form-data"})
    @Operation(summary = "Preview upload restaurant menu", description = "API for previewing uploaded restaurant menu")
    public ResponseEntity<ApiResponse> previewUploadMenu(@RequestParam MultipartFile file) {
        try {
            User user = userService.getAuthenticatedUser();
            List<MenuDto> results =  menuService.previewUploadedMenu(file, user.getId());
            return ResponseEntity.ok(new ApiResponse("List Menu: ", results));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload restaurant menu", description = "API for uploading restaurant menu")
    public ResponseEntity<ApiResponse> uploadMenu(@RequestParam MultipartFile file) {
        try {
            User user = userService.getAuthenticatedUser();
            List<MenuDto> results =  menuService.uploadMenu(file, user.getId());
            return ResponseEntity.ok(new ApiResponse("List Menu: ", results));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(value = "/edit-availability")
    public ResponseEntity<ApiResponse> editAvailability(@RequestParam UUID menuCode) {
        try {
            User user = userService.getAuthenticatedUser();
            menuService.editMenuAvailability(menuCode, user.getId());
            return ResponseEntity.ok(new ApiResponse("List Menu: ", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping(value = "/edit-all-availability")
    public ResponseEntity<ApiResponse> editAllAvailability(@RequestParam UUID restaurantId, @RequestParam boolean activate) {
        try {
            User user = userService.getAuthenticatedUser();
            menuService.editAllMenuAvailability(restaurantId, user.getId(), activate);
            return ResponseEntity.ok(new ApiResponse("List Menu: ", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/get-menu-by-code")
    @Operation(summary = "Show Menus by Menu Code", description = "API to display menus by providing menu code")
    public ResponseEntity<ApiResponse> getMenuByCode(@RequestParam UUID[] menuCodes) {
        try {
            CartMenuDto result = menuService.getMenuByCode(menuCodes);
            return ResponseEntity.ok(new ApiResponse("Result: ", result));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/get-menus")
    @Operation(summary = "Show Menus by Restaurant ID", description = "API to display menus by providing restaurant ID")
    public ResponseEntity<ApiResponse> getActiveMenuByRestaurant(@RequestParam UUID restaurantId) {
        try {
            List<Menu> menus = menuService.getAllActiveMenu(restaurantId);
            List<MenuDto> convertedMenus = menuService.getConvertedMenus(menus);
            return ResponseEntity.ok(new ApiResponse("List of menus: ", convertedMenus));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/get-menus-by-restaurant")
    @Operation(summary = "Show Menus by Restaurant ID", description = "API to display menus by providing restaurant ID")
    public ResponseEntity<ApiResponse> getAllMenusOfRestaurant(@RequestParam UUID restaurantId) {
        try {
            List<Menu> menus = menuService.getMenusByRestaurantId(restaurantId);
            List<MenuDto> convertedMenus = menuService.getConvertedMenus(menus);
            return ResponseEntity.ok(new ApiResponse("List of menus: ", convertedMenus));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/get-top-5-menus")
    @Operation(summary = "Get top 5 menus of restaurant", description = "API to display top 5 menus by restaurant id")
    public ResponseEntity<ApiResponse> getTop5Menus(@RequestParam UUID restaurantId) {
        try {
            User user = userService.getAuthenticatedUser();
            List<TopMenusDto> topMenusDtos = menuService.getTop5Menu(restaurantId, user.getId());

            return ResponseEntity.ok(new ApiResponse("List of menus: ", topMenusDtos));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/detail")
    @Operation(summary = "Get menu detail", description = "Get details of a menu")
    public ResponseEntity<ApiResponse> getMenuDetail(@RequestParam UUID menuCode) {
        try {
            User user = userService.getAuthenticatedUser();
            Menu result = menuService.getMenuDetail(menuCode, user.getId());
            return ResponseEntity.ok(new ApiResponse("List of menus: ", result));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

}
