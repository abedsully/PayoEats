package com.example.PayoEat_BE.controller;

import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.dto.RestaurantDto;
import com.example.PayoEat_BE.model.Image;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.request.menu.UpdateMenuRequest;
import com.example.PayoEat_BE.request.restaurant.UpdateRestaurantRequest;
import com.example.PayoEat_BE.response.ApiResponse;
import com.example.PayoEat_BE.service.image.IImageService;
import com.example.PayoEat_BE.service.menu.IMenuService;
import com.example.PayoEat_BE.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
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
    private final IImageService imageService;
    private final IUserService userService;

    @PostMapping(value = "/add-menu", consumes = {"multipart/form-data"})
    @Operation(summary = "Add Menu in Restaurant", description = "API for adding menu in restaurant")
    public ResponseEntity<ApiResponse> addMenu(
            @RequestParam("menuName") String menuName,
            @RequestParam("menuDetail") String menuDetail,
            @RequestParam("menuPrice") double menuPrice,
            @RequestParam("restaurantId") UUID restaurantId,
            @RequestParam("menuImage") MultipartFile menuImage) {
        try {
            User user = userService.getAuthenticatedUser();
            AddMenuRequest request = new AddMenuRequest(menuName, menuDetail, menuPrice, restaurantId, user.getId());
            Menu menu = menuService.addMenu(request, menuImage);
            MenuDto convertedMenu = menuService.convertToDto(menu);
            return ResponseEntity.ok(new ApiResponse("Menu successfully added", convertedMenu));
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

    @GetMapping("/image")
    @Operation(summary = "Show Menu Image by ID", description = "API to display menu image by providing the image ID")
    public ResponseEntity<ByteArrayResource> showMenuImage(@RequestParam UUID imageId) {
        try {
            Image image = imageService.getImageById(imageId);

            ByteArrayResource resource = new ByteArrayResource(image.getImage());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, image.getFileType()).body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
    }

    @GetMapping("/get-menus")
    @Operation(summary = "Show Menus by Restaurant ID", description = "API to display menus by providing restaurant ID")
    public ResponseEntity<ApiResponse> getMenusByRestaurantId(@RequestParam UUID restaurantId) {
        try {
            List<Menu> menus = menuService.getMenusByRestaurantId(restaurantId);
            List<MenuDto> convertedMenus = menuService.getConvertedMenus(menus);
            return ResponseEntity.ok(new ApiResponse("List of menus: ", convertedMenus));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete-menu")
    @Operation(summary = "Delete a menu by Menu Code", description = "API to delete restaurant menu by providing its code")
    public ResponseEntity<ApiResponse> deleteMenu(@RequestParam UUID menuCode) {
        try {
            menuService.deleteMenu(menuCode);
            return ResponseEntity.ok(new ApiResponse("Menu deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping(value = "/update-menu", consumes = {"multipart/form-data"})
    @Operation(summary = "Update a menu by Menu Code", description = "API to update restaurant menu by providing its code")
    public ResponseEntity<ApiResponse> updateMenu(
            @RequestParam UUID menuCode,
            @RequestParam String menuName,
            @RequestParam String menuDetail,
            @RequestParam double menuPrice,
            @RequestParam MultipartFile menuImage) {
        try {
            UpdateMenuRequest request = new UpdateMenuRequest(menuName, menuDetail, menuPrice);
            Menu menu = menuService.updateMenu(menuCode, request, menuImage);
            MenuDto convertedMenu = menuService.convertToDto(menu);
            return ResponseEntity.ok(new ApiResponse("Menu updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
