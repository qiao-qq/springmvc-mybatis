package com.my.ssm.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.my.ssm.po.ItemsCustom;
import com.my.ssm.po.ItemsQueryVo;
import com.my.ssm.service.ItemsService;

@Controller
@RequestMapping("/items")
public class ItemsController {

	@Autowired
	private ItemsService itemsService;

	// 商品查询
	@RequestMapping(value = "/queryItems")
	public ModelAndView queryItems(HttpServletRequest request, ItemsQueryVo itemsQueryVo) throws Exception {

		// 测试forward后request是否可以共享
		System.out.println(request.getParameter("id"));

		// 调用service查找 数据库，查询商品列表
		List<ItemsCustom> itemsList = itemsService.findItemsList(itemsQueryVo);

		// 返回ModelAndView
		ModelAndView modelAndView = new ModelAndView();
		// 相当 于request的setAttribut，在jsp页面中通过itemsList取数据
		modelAndView.addObject("itemsList", itemsList);
		// 指定视图
		modelAndView.setViewName("items/itemsList");
		return modelAndView;
	}

	// 商品信息修改页面显示
	// 限制http请求方法,可以post和get
//	@RequestMapping(value = "/editItems", method = { RequestMethod.POST, RequestMethod.GET })
//	public ModelAndView editItems() throws Exception {
//		// 调用service根据商品id查询商品信息
//		ItemsCustom itemsCustom = itemsService.findItemsById(1);
//		// 返回ModelAndView
//		ModelAndView modelAndView = new ModelAndView();
//		// 将商品信息放到model
//		modelAndView.addObject("itemsCustom", itemsCustom);
//		// 商品修改页面
//		modelAndView.setViewName("items/editItems");
//		return modelAndView;
//	}

	// ModelAndView改为String
	@RequestMapping(value = "/editItems", method = { RequestMethod.POST, RequestMethod.GET })
	// @RequestParam里边指定request传入参数名称和形参进行绑定。
	// 通过required属性指定参数是否必须要传入
	// 通过defaultValue可以设置默认值，如果id参数没有传入，将默认值和形参绑定。
	public String editItems(Model model, @RequestParam(value = "id", required = true) Integer items_id)
			throws Exception {
		// 调用service根据商品id查询商品信息
		ItemsCustom itemsCustom = itemsService.findItemsById(items_id);

		// 通过形参中的model将model数据传到页面
		// 相当于modelAndView.addObject方法
		model.addAttribute("items", itemsCustom);
		return "items/editItems";
	}

	// 商品信息修改提交
//	@RequestMapping("/editItemsSubmit")
//	public ModelAndView editItemsSubmit() throws Exception {
//		// 调用service更新商品信息，页面需要将商品信息传到此方法
//		// 指定视图
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("success");
//		return modelAndView;
//	}
	// 改为String
	@RequestMapping("/editItemsSubmit")
	public String editItemsSubmit(HttpServletRequest request, Integer id, ItemsCustom itemsCustom,
			MultipartFile items_pic) throws Exception {
		// MultipartFile items_pic 接受图片
		// 上传图片
		// 原始名称
		String originalFilename = items_pic.getOriginalFilename();

		if (items_pic != null && originalFilename != null && originalFilename.length() > 0) {

			// 存储图片的物理路径
			String pic_path = "F:\\eclipse\\picture\\";
			// 新的图片名称
			String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
			// 新图片
			File newFile = new File(pic_path + newFileName);

			// 将内存中的数据写入磁盘
			items_pic.transferTo(newFile);

			// 将新图片名称写到itemsCustom中
			itemsCustom.setPic(newFileName);

		}

		// 调用service更新商品信息，页面需要将商品信息传到此方法
		itemsService.updateItems(id, itemsCustom);
		// 重定向到商品查询列表
		// return "redirect:queryItems";
		// 页面转发
		// return "forward:queryItems";
		return "success";
	}

	// 批量删除商品信息
	@RequestMapping("/deleteItems")
	public String deleteItems(Integer[] items_id) throws Exception {
		// 调用service批量删除商品
		// ...
		return "success";
	}

	// 批量修改商品页面，将商品信息查询出来，在页面中可以编辑商品信息
	@RequestMapping(value = "/editItemsQuery")
	public ModelAndView editItemsQuery(HttpServletRequest request, ItemsQueryVo itemsQueryVo) throws Exception {

		// 调用service查找 数据库，查询商品列表
		List<ItemsCustom> itemsList = itemsService.findItemsList(itemsQueryVo);

		// 返回ModelAndView
		ModelAndView modelAndView = new ModelAndView();
		// 相当 于request的setAttribut，在jsp页面中通过itemsList取数据
		modelAndView.addObject("itemsList", itemsList);
		// 指定视图
		modelAndView.setViewName("items/editItemsQuery");
		return modelAndView;
	}

	// 批量修改商品提交页面
	// 通过ItemsQueryVo接收批量提交的商品信息，将商品信息存储到itemsQueryVo中itemsList属性中。
	@RequestMapping("/editItemsAllSubmit")
	public String editItemsAllSubmit(ItemsQueryVo itemsQueryVo) throws Exception {

		return "success";
	}

	// 查询商品信息，输出json
	/// itemsView/{id}里边的{id}表示占位符，通过@PathVariable获取占位符中的参数，
	// 如果占位符中的名称和形参名一致，在@PathVariable可以不指定名称
	@RequestMapping("/itemsView/{id}")
	public @ResponseBody ItemsCustom itemsView(@PathVariable("id") Integer id) throws Exception {

		// 调用service查询商品信息
		ItemsCustom itemsCustom = itemsService.findItemsById(id);

		return itemsCustom;

	}
}
