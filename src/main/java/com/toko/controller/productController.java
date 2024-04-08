package com.toko.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.toko.dao.CategoryDAO;
import com.toko.dao.ProductDAO;
import com.toko.dao.ProductDetailDAO;
import com.toko.entity.Category;
import com.toko.entity.Product;
import com.toko.entity.ProductDetail;
import com.toko.service.CategoryService;
import com.toko.service.ProductService;

@Controller
public class productController {
	@Autowired
	ProductDAO proDAO;
	@Autowired
	CategoryDAO cateDAO;
	@Autowired
	CategoryService cateSer;
	@Autowired
	ProductService proSer;
	@Autowired
	ProductDetailDAO detailSevice;
	

	@RequestMapping("/")
	public String trangChu(Model model) {
		List<Product> list = proDAO.findAll();
		List<Product> list1 = proDAO.findAllStartingWithAoThun();
		List<Product> list2 = proDAO.findAllStartingWithAoPolo();
		List<Product> list3 = proDAO.findAllStartingWithAoKhoac();
		List<Product> list4 = proDAO.findAllStartingWithAoSoMi();
		List<Product> list5 = proDAO.findAllStartingWithPhuKien();
		model.addAttribute("items", list1);
		model.addAttribute("itemPolo", list2);
		model.addAttribute("itemKhoac", list3);
		model.addAttribute("itemAoSoMi", list4);
		model.addAttribute("itemPhuKien", list5);
		model.addAttribute("itemCategory", list);
		return "/home/trangChu";
	}

	@GetMapping("/product/detail/{productId}")
	public String productDetail(@PathVariable Integer productId, Model model) {
		Product Products = proDAO.findProductById(productId);
		List<ProductDetail> sizes = detailSevice.findByProductId(productId);
		List<ProductDetail> qty = detailSevice.findByProductId(productId);

		model.addAttribute("Product", Products);
		model.addAttribute("sizes", sizes);
		model.addAttribute("qty", qty);

		return "/product/detail";

	}

//	@GetMapping("/product/list/all")
//	public String trangChu(Model model) {
//		model.addAttribute("category",cateDAO.findAll());
//		return "/product/list";
//	}

	@RequestMapping("/product/list/listCategory")
	public String listByCategory(
			@RequestParam(value = "category", required = false, defaultValue = "all") String category, Model model,
			@RequestParam("page") Optional<Integer> page) {
		List<Product> list;
		if ("all".equals(category)) {
			list = proSer.findAll();
			category = "all";
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		} else {
			list = getProductListByCategory(category);
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "/product/list";
	}

	private List<Product> getProductListByCategory(String category) {
		switch (category) {
		case "Áo thun":
			return proSer.findAllStartingWithAoThun();
		case "Áo Polo":
			return proSer.findAllStartingWithAoPolo();
		case "Baby Tee":
			return proSer.findAllStartingWithAoBabyTee();
		case "Áo sơ mi":
			return proSer.findAllStartingWithAoSoMi();
		case "Áo khoác":
			return proSer.findAllStartingWithAoKhoac();
		case "Hoodie":
			return proSer.findAllStartingWithAoHoodie();
		case "Quần":
			return proSer.findAllStartingWithQuan();
		case "Phụ kiện":
			return proSer.findAllStartingWithPhuKien();
		default:
			return Collections.emptyList();
		}
	}

//	@RequestMapping("/product/list/sortByNameLow")
//	public String sortByNameLow(Model model, Pageable pageable) {
//		Page<Product> list = proDAO.findBySortNameLow(pageable);
//		model.addAttribute("items", list);
//		
//		return "/product/list";
//	}

	@RequestMapping("/product/list/sortByNameAZ")
	public String sortByNameLow(Model model, Pageable pageable,
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			@RequestParam("page") Optional<Integer> page) {
		if ("all".equals(category)) {
			category = "all";
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			List<Product> list = proSer.findBySortNameLowAll();
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		} else {
			Category cate = cateSer.findByType(category);
			List<Product> list = proSer.findBySortNameLowCategory(cate);
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "/product/list";
	}

	@RequestMapping("/product/list/sortByNameZA")
	public String sortByNameHigh(Model model, Pageable pageable,
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			@RequestParam("page") Optional<Integer> page) {
		if ("all".equals(category)) {
			category = "all";
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			List<Product> list = proSer.findBySortNameHighAll();
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		} else {
			Category cate = cateSer.findByType(category);
			List<Product> list = proSer.findBySortNameHighCategory(cate);
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "/product/list";
	}

	@RequestMapping("/product/list/sortByPriceLow")
	public String sortByPriceLow(Model model, Pageable pageable,
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			@RequestParam("page") Optional<Integer> page) {
		if ("all".equals(category)) {
			category = "all";
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			List<Product> list = proSer.findBySortPriceLowAll();
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		} else {
			Category cate = cateSer.findByType(category);
			List<Product> list = proSer.findBySortPriceLowCategory(cate);
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "/product/list";
	}

	@RequestMapping("/product/list/sortByPriceHigh")
	public String sortByPriceHigh(Model model, Pageable pageable,
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			@RequestParam("page") Optional<Integer> page) {
		if ("all".equals(category)) {
			category = "all";
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			List<Product> list = proSer.findBySortPriceHighAll();
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		} else {
			Category cate = cateSer.findByType(category);
			List<Product> list = proSer.findBySortPriceHighCategory(cate);
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "/product/list";
	}

	@RequestMapping("/product/list/sortByPriceBetween02")
	public String sortByPriceBetween02(Model model, Pageable pageable,
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			@RequestParam("page") Optional<Integer> page) {
		if ("all".equals(category)) {
			category = "all";
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			List<Product> list = proSer.findBySortPriceBetween02All();
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		} else {
			Category cate = cateSer.findByType(category);
			List<Product> list = proSer.findBySortPriceBetween02Category(cate);
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "/product/list";
	}

	@RequestMapping("/product/list/sortByPriceBetween25")
	public String sortByPriceBetween25(Model model, Pageable pageable,
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			@RequestParam("page") Optional<Integer> page) {
		if ("all".equals(category)) {
			category = "all";
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			List<Product> list = proSer.findBySortPriceBetween25All();
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		} else {
			Category cate = cateSer.findByType(category);
			List<Product> list = proSer.findBySortPriceBetween25Category(cate);
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "/product/list";
	}

	@RequestMapping("/product/list/sortByPriceBetween5up")
	public String sortByPriceBetween5up(Model model, Pageable pageable,
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			@RequestParam("page") Optional<Integer> page) {
		if ("all".equals(category)) {
			category = "all";
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			List<Product> list = proSer.findBySortPriceBetween5upAll();
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		} else {
			Category cate = cateSer.findByType(category);
			List<Product> list = proSer.findBySortPriceBetween5upCategory(cate);
			int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), list);
			model.addAttribute("items", listProduct);
			model.addAttribute("category", category);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}
		return "/product/list";
	}

	@RequestMapping("/product/list/findByName")
	public String findByName(Model model, String keyword, 
			
			@RequestParam("page") Optional<Integer> page) {
	    if (keyword != null && !keyword.isEmpty()) {
	        List<Product> listProByName = proSer.findByName("%" + keyword + "%");
	        int currentPage = page.orElse(1);
			int pageSize = 16;
			model.addAttribute("Page", currentPage);
			Page<Product> listProduct = proSer.findPaginated(PageRequest.of(currentPage - 1, pageSize), listProByName);
			model.addAttribute("items", listProduct);
			model.addAttribute("keyWord", keyword);
			int totalPages = listProduct.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
	        if (listProByName != null && !listProByName.isEmpty()) {
	            model.addAttribute("findProByName", listProByName);
	        } else {
	            model.addAttribute("message", "Không tìm thấy sản phẩm phù hợp");
	        }
	    } else {
	        model.addAttribute("message", "Vui lòng nhập từ khóa tìm kiếm");
	    }
	    return "/product/listWhenFindByName";
	}




	@RequestMapping("/product/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		Product product = proSer.findById(id);
		model.addAttribute("item", product);
		return "detail";
	}

}
