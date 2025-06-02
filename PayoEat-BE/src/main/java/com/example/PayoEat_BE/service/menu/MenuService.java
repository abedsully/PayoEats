package com.example.PayoEat_BE.service.menu;

import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Image;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.MenuRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.request.menu.UpdateMenuRequest;
import com.example.PayoEat_BE.service.image.ImageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class MenuService implements IMenuService{
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public List<MenuDto> getMenuByCode(UUID[] menuCodes) {

        List<MenuDto> menuDtoList = new ArrayList<>();
        for (UUID menuCode : menuCodes) {
            Menu menu =  menuRepository.findByMenuCodeAndIsActiveTrue(menuCode)
                    .orElseThrow(() -> new NotFoundException("Menu not found with id: " + menuCode));

            MenuDto convertedMenu = convertToDto(menu);

            menuDtoList.add(convertedMenu);
        }
        return menuDtoList;
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

        return menuRepository.findByRestaurantId(restaurant.getId());
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

    @Override
    public List<MenuDto> previewUploadedMenu(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with user id: " + userId));

        if (!user.getRoles().equals(UserRoles.RESTAURANT) || !user.getId().equals(restaurant.getUserId())) {
            throw new ForbiddenException("Unauthorized request");
        }

        String returnMessage = "";
        long totalRecords = 0L;

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            List<Menu> menuList = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row == null) continue;

                Menu menu = new Menu();
                menu.setMenuName(getCellValue(row.getCell(0)));
                menu.setMenuDetail(getCellValue(row.getCell(2)));
                menu.setMenuPrice(Double.parseDouble(getCellValue(row.getCell(1))));
                menu.setMenuImageUrl(getCellValue(row.getCell(3)));
                menu.setCreatedAt(LocalDateTime.now());
                menu.setRestaurant(restaurant);

                menuList.add(menu);
            }

            return getConvertedMenus(menuList);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public List<MenuDto> uploadMenu(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Restaurant restaurant = restaurantRepository.findByUserIdAndIsActiveTrue(user.getId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with user id: " + userId));

        if (!user.getRoles().equals(UserRoles.RESTAURANT) || !user.getId().equals(restaurant.getUserId())) {
            throw new ForbiddenException("Unauthorized request");
        }

        String returnMessage = "";
        long totalRecords = 0L;

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            List<Menu> menuList = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row == null) continue;

                Menu menu = new Menu();
                menu.setMenuName(getCellValue(row.getCell(0)));
                menu.setMenuDetail(getCellValue(row.getCell(2)));
                menu.setMenuPrice(Double.parseDouble(getCellValue(row.getCell(1))));
                menu.setMenuImageUrl(getCellValue(row.getCell(3)));
                menu.setCreatedAt(LocalDateTime.now());
                menu.setRestaurant(restaurant);
                menu.setIsActive(true);
                menuList.add(menu);
            }

            menuRepository.saveAll(menuList);

            return getConvertedMenus(menuList);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
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
            imageService.updateImage(menuImage, existingMenu.getMenuCode());
        }

        existingMenu.setUpdatedAt(LocalDateTime.now());

        return existingMenu;
    }

    private Menu createMenu(AddMenuRequest request, MultipartFile menuImage) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + request.getUserId()));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(request.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + request.getRestaurantId()));

        if (!user.getRoles().equals(UserRoles.RESTAURANT) || !user.getId().equals(restaurant.getUserId())) {
            throw new ForbiddenException("Unauthorized request");
        }

        Menu menu = new Menu(
                request.getMenuName(),
                request.getMenuDetail(),
                request.getMenuPrice()
        );

        menu.setCreatedAt(LocalDateTime.now());
        menu.setIsActive(true);
        menu.setRestaurant(restaurant);

        Image image = imageService.saveMenuImage(menuImage, menu.getMenuCode());
        image.setMenuCode(menu.getMenuCode());
        menu.setMenuImage(image.getId());

        return menuRepository.save(menu);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                return String.format("%.0f", numericValue);
            default:
                return "";
        }
    }
}
