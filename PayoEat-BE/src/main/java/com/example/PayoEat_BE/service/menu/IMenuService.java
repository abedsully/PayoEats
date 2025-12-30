package com.example.PayoEat_BE.service.menu;

import com.example.PayoEat_BE.dto.CartMenuDto;
import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.dto.TopMenusDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.request.menu.UpdateMenuRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IMenuService {
    CartMenuDto getMenuByCode(UUID[] menuCodes);
    UUID addMenu(AddMenuRequest request, MultipartFile file, Long userId);
    UUID editMenu(AddMenuRequest request, MultipartFile file, Long userId, UUID menuCode);
    MenuDto convertToDto(Menu menu);
    List<MenuDto> getConvertedMenus(List<Menu> menus);
    List<Menu> getAllActiveMenu(UUID restaurantId);
    List<Menu> getMenusByRestaurantId(UUID restaurantId);
    List<TopMenusDto> getTop5Menu(UUID restaurantId, Long userId);
    void editMenuAvailability(UUID menuCode, Long userId);
    void editAllMenuAvailability(UUID restaurantId, Long userId, Boolean activate);
    Menu getMenuDetail(UUID menuCode, Long userId);
}
