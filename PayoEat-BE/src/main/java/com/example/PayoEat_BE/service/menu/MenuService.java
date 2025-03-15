package com.example.PayoEat_BE.service.menu;

import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Image;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.request.menu.UpdateMenuRequest;
import com.example.PayoEat_BE.service.image.ImageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class MenuService implements IMenuService{
    private final com.example.PayoEat_BE.repository.MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Override
    public Menu getMenuById(String menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu not found with id: " + menuId));
    }

    @Override
    public Menu addMenu(AddMenuRequest request, MultipartFile menuImage) {
        return menuRepository.save(createMenu(request, menuImage));
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
    public List<Menu> getMenusByRestaurantId(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        return menuRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public void deleteMenu(UUID menuCode) {
        menuRepository.findByMenuCodeAndIsActiveTrue(menuCode)
                .map(currentMenu -> {
                    deleteExistingMenu(currentMenu);
                    return menuRepository.save(currentMenu);
                })
                .orElseThrow(() -> new NotFoundException("Menu not found"));

    }

    private void deleteExistingMenu(Menu existingMenu) {
        existingMenu.setUpdatedAt(LocalDateTime.now());
        existingMenu.setIsActive(false);
    }


    @Override
    public Menu updateMenu(UUID menuCode, UpdateMenuRequest request, MultipartFile menuImage) {
       return menuRepository.findByMenuCodeAndIsActiveTrue(menuCode)
                .map(existingMenu -> updateExistingMenu(existingMenu, request, menuImage))
                .map(menuRepository::save)
                .orElseThrow(() -> new NotFoundException("Menu not found with code: " + menuCode));

    }

    private Menu updateExistingMenu(Menu existingMenu, UpdateMenuRequest request, MultipartFile menuImage) {

        if ((request.getMenuName() == null || request.getMenuName().isEmpty()) &&
                request.getMenuPrice() == null &&
                (request.getMenuDetail() == null || request.getMenuDetail().isEmpty()) &&
                menuImage == null) {
            throw new IllegalArgumentException("No valid fields provided to update the menu.");
        }

        if (request.getMenuName() != null && !request.getMenuName().isEmpty()) {
            existingMenu.setMenuName(request.getMenuName());
        }

        if (request.getMenuPrice() != null) {
            existingMenu.setMenuPrice(request.getMenuPrice());
        }

        if (request.getMenuDetail() != null && !request.getMenuDetail().isEmpty()) {
            existingMenu.setMenuDetail(request.getMenuDetail());
        }

        if (menuImage != null) {
            imageService.updateImage(menuImage, existingMenu.getMenuImage().getId());
        }

        existingMenu.setUpdatedAt(LocalDateTime.now());

        return existingMenu;
    }

    private Menu createMenu(AddMenuRequest request, MultipartFile menuImage) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + request.getRestaurantId()));


        Menu menu = new Menu(
                request.getMenuName(),
                request.getMenuDetail(),
                request.getMenuPrice()
        );

        menu.setCreatedAt(LocalDateTime.now());
        menu.setIsActive(true);
        menu.setRestaurant(restaurant);

        Image image = imageService.saveMenuImage(menuImage, menu.getMenuCode());
        image.setMenu(menu);
        menu.setMenuImage(image);

        return menuRepository.save(menu);
    }
}
