package com.example.PayoEat_BE.service;

import com.example.PayoEat_BE.dto.CartMenuDto;
import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.dto.TopMenusDto;
import com.example.PayoEat_BE.dto.restaurants.CheckUserRestaurantDto;
import com.example.PayoEat_BE.enums.UploadType;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.repository.MenuRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.service.UploadService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class MenuService implements IMenuService{
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    private final UploadService uploadService;


    @Override
    public CartMenuDto getMenuByCode(UUID[] menuCodes) {

        List<MenuDto> menuDtoList = menuRepository.findMenuDtosByCodes(Arrays.asList(menuCodes));

        if (menuDtoList.isEmpty()) {
            throw new NotFoundException("No menus found");
        }

        CartMenuDto dto = new CartMenuDto();

        MenuDto firstMenu = menuDtoList.get(0);
        dto.setRestaurantName(firstMenu.getRestaurantName());
        dto.setRestaurantId(firstMenu.getRestaurantId());
        dto.setMenuDtos(menuDtoList);

        return dto;
    }


    @Override
    public UUID addMenu(AddMenuRequest request, MultipartFile file, Long userId) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);

        String menuImageUrl = uploadService.upload(file, UploadType.MENU);

        Menu menu = new Menu();
        if (!menuImageUrl.isEmpty() || !menuImageUrl.isBlank()) {
            menu.setMenuImageUrl(menuImageUrl);
        }

        menu.setMenuName(request.getMenuName());
        menu.setMenuDetail(request.getMenuDetail());
        menu.setMenuPrice(request.getMenuPrice());
        menu.setIsActive(request.getIsActive());
        menu.setRestaurantId(user.getId());

        return menuRepository.addMenu(menu);
    }

    @Override
    public UUID editMenu(AddMenuRequest request, MultipartFile file, Long userId, UUID menuCode) {

        CheckUserRestaurantDto user = checkUserRestaurant(userId);

        Menu existing = menuRepository.getMenuDetail(menuCode);

        boolean changed = false;

        String menuImageUrl = existing.getMenuImageUrl();

        if (file != null && !file.isEmpty()) {
            menuImageUrl = uploadService.upload(file, UploadType.MENU);
            changed = true;
        }

        if (!existing.getMenuName().equals(request.getMenuName())) {
            changed = true;
        }

        if (!existing.getMenuDetail().equals(request.getMenuDetail())) {
            changed = true;
        }

        if (!existing.getMenuPrice().equals(request.getMenuPrice())) {
            changed = true;
        }

        if (!existing.getIsActive().equals(request.getIsActive())) {
            changed = true;
        }

        if (!changed) {
            return existing.getMenuCode();
        }

        Menu menu = new Menu();
        menu.setMenuName(request.getMenuName());
        menu.setMenuDetail(request.getMenuDetail());
        menu.setMenuPrice(request.getMenuPrice());
        menu.setIsActive(request.getIsActive());
        menu.setRestaurantId(user.getId());
        menu.setMenuImageUrl(menuImageUrl);

        return menuRepository.updateMenu(menu, menuCode);
    }


    @Override
    public MenuDto convertToDto(Menu menu) {
        return modelMapper.map(menu, MenuDto.class);
    }

    @Override
    public List<MenuDto> getConvertedMenus(List<Menu> menus) {
        return menus.stream().map(this::convertToDto).toList();
    }

    @Override
    public List<Menu> getAllActiveMenu(UUID restaurantId) {
        return menuRepository.getAllActiveMenu(restaurantId);
    }

    @Override
    public List<Menu> getMenusByRestaurantId(UUID restaurantId) {
        return menuRepository.getMenusByRestaurantId(restaurantId);
    }

    @Override
    public List<TopMenusDto> getTop5Menu(UUID restaurantId, Long userId) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);

        return menuRepository.getTop5MenusFromRestaurant(restaurantId);
    }

    @Override
    public void editMenuAvailability(UUID menuCode, Long userId) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);

        menuRepository.editMenuAvailability(menuCode);
    }

    @Override
    public void editAllMenuAvailability(UUID restaurantId, Long userId, Boolean activate) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);

        menuRepository.updateAllMenuAvailability(restaurantId, activate);
    }

    @Override
    public Menu getMenuDetail(UUID menuCode, Long userId) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);

        return menuRepository.getMenuDetail(menuCode);
    }

    @Override
    public void deleteMenu(UUID menuCode, Long userId) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);

        int result = menuRepository.deleteMenu(menuCode);

        if (result != 1) {
            throw new InvalidException("Failed to delete this menu, please try again");
        }
    }


    private CheckUserRestaurantDto checkUserRestaurant(Long userId) {
        CheckUserRestaurantDto result = restaurantRepository.checkUserRestaurant(userId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        if (result.getRoleId() != 2L || !result.getUserId().equals(userId)) {
            throw new ForbiddenException("Unauthorized! You can't access this restaurant");
        }

        return result;
    }

}
