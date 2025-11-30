package com.example.PayoEat_BE.service.menu;

import com.example.PayoEat_BE.dto.CartMenuDto;
import com.example.PayoEat_BE.dto.MenuDto;
import com.example.PayoEat_BE.dto.TopMenusDto;
import com.example.PayoEat_BE.dto.restaurants.CheckUserRestaurantDto;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Menu;
import com.example.PayoEat_BE.repository.MenuRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.request.menu.AddMenuRequest;
import com.example.PayoEat_BE.service.UploadService;
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

        String menuImageUrl = uploadService.upload(file);

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
    public List<MenuDto> previewUploadedMenu(MultipartFile file, Long userId) throws IOException {
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
                menu.setRestaurantId(UUID.randomUUID());

                menuList.add(menu);
            }

            return getConvertedMenus(menuList);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public List<MenuDto> uploadMenu(MultipartFile file, Long userId) throws IOException {
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
                menu.setRestaurantId(UUID.randomUUID());
                menu.setIsActive(true);
                menuList.add(menu);
            }

            menuRepository.addMenus(menuList);

            return getConvertedMenus(menuList);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
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
    public Menu getMenuDetail(UUID menuCode, Long userId) {
        CheckUserRestaurantDto user = checkUserRestaurant(userId);

        return menuRepository.getMenuDetail(menuCode);
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

    private CheckUserRestaurantDto checkUserRestaurant(Long userId) {
        CheckUserRestaurantDto result = restaurantRepository.checkUserRestaurant(userId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        if (result.getRoleId() != 2L || !result.getUserId().equals(userId)) {
            throw new ForbiddenException("Unauthorized! You can't access this restaurant");
        }

        return result;
    }

}
