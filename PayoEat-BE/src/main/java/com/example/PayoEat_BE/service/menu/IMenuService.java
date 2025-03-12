package com.example.PayoEat_BE.service.menu;

import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.request.menu.UpdateMenuRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IMenuService {
    Menu getMenuById(String menuId);
    Menu addMenu(AddMenuRequest request, MultipartFile menuImage);
    MenuDto convertToDto(Menu menu);
    List<MenuDto> getConvertedMenus(List<Menu> menus);
    List<Menu> getMenusByRestaurantId(UUID restaurantId);
    void deleteMenu(UUID menuCode);
    Menu updateMenu(UUID menuCode, UpdateMenuRequest request, MultipartFile menuImage);
}
