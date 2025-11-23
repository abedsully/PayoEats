package com.example.PayoEat_BE.service.menu;

import com.example.PayoEat_BE.dto.CartMenuDto;
import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.request.menu.UpdateMenuRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IMenuService {
    CartMenuDto getMenuByCode(UUID[] menuCodes);
    UUID addMenu(AddMenuRequest request);
    MenuDto convertToDto(Menu menu);
    List<MenuDto> getConvertedMenus(List<Menu> menus);
    List<Menu> getMenusByRestaurantId(UUID restaurantId);
    List<MenuDto> previewUploadedMenu(MultipartFile file, Long userId) throws IOException;
    List<MenuDto> uploadMenu(MultipartFile file, Long userId) throws IOException;
}
