/**
 * @file products.js
 * @description 商品頁面的主要邏輯
 */

/**
 * 全域變量
 */
let maxId = 0;

/**
 * @description 引入根據ID產生貨號的模塊
 */
import { generateCodeFromInteger } from "./rand-str.js"; //產生貨號的前置
// console.log(`輸入 456975: ${generateCodeFromInteger(4569)}`); //應該是AGTT

/**
 * @description 引入連接後端的模塊
 */
import {
  fetchCategoriesProd,
  fetchProducts,
  saveProduct,
  fetchImage,
  fetchSku,
} from "./api-client.js";

/**
 * @description 引入cate相關的function
 */
import { buildCategoryTree, generateTreeHTML } from "./prod-cate.js";

/**
 * 將選中的分類id和name代入至搜索的form中
 * @param {*} id
 * @param {*} name
 */
function cateEditFilter(id, name, isParent) {
  if (isParent == "false") {
    document.getElementById("prod-cate-id").value = id;
    document.getElementById("cate-filter").value = name;
  }
}

function setCateDefault() {
  document.getElementById("prod-cate-id").value = "";
  document.getElementById("cate-filter").value = "全部類別";
}

function newProd() {
  console.log("新增商品");
  document.getElementById("product-edit-head").innerText = "新增商品";
  resetEditForm();
  imgEdit("new");
  skuEdit("new");
}

window.editProduct = async function (product) {
  resetEditForm();
  document.getElementById("product-edit-head").innerText =
    "編輯商品:" + product["prod_name"];
  console.log(product);
  document.getElementById("prod-id-edit").value = product["prod_id"];
  document.getElementById("prod-name").value = product["prod_name"];
  document.getElementById("prod-cate-select").value = product["prod_cate_id"];
  document.getElementById("prod-desc").value = product["prod_desc"];

  switch (product["prod_status"]) {
    case 0:
      // 選擇「未上架」
      document.getElementById("statusEmpty").checked = true;
      break;
    case 1:
      // 選擇「上架中」
      document.getElementById("statusON").checked = true;
      break;
    case 2:
      // 選擇「已下架」
      document.getElementById("statusOff").checked = true;
      break;

    default:
      break;
  }
  const newProducts = await fetchImage(product["prod_id"]);
  const newSkus = await fetchSku(product["prod_id"]);

  // console.log(newProducts);

  // 載入圖片
  imgEdit("edit", newProducts);
  skuEdit("edit", product["prod_id"], newSkus);

  // 載入SKU
};

function imgEdit(type, arr = []) {
  const addImageBtn = document.getElementById("addImageBtn");
  const imageContainer = document.getElementById("imageContainer");

  /**
   * 建立一個新的圖片卡片的 HTML 字串 (使用全行內樣式)
   * @returns {string} HTML string
   */
  const createNewImageCard = (
    imgSrc = "https://placehold.co/200x200/6c757d/white?text=點我上傳",
    imgId = ""
  ) => {
    // 使用時間戳和隨機數確保 ID 的唯一性
    const uniqueId = `file-input-${Date.now()}-${Math.random()
      .toString(36)
      .substring(2, 9)}`;
    const placeholderImg =
      "https://placehold.co/200x200/dc3545/white?text=圖片載入失敗";

    return `
                <div data-role="image-card" data-id="${imgId}" style="position: relative; width: 200px; height: 200px; margin: 1rem; box-shadow: 0 4px 8px rgba(0,0,0,0.1); transition: transform 0.2s ease-in-out; transform: none;">
                    <!-- 刪除按鈕，使用 data-action 屬性供 JS 選取 -->
                    <button type="button" data-action="delete" aria-label="刪除此圖片" style="position: absolute; top: -10px; right: -10px; z-index: 10; border-radius: 50%; width: 32px; height: 32px; padding: 0; display: flex; align-items: center; justify-content: center; font-size: 1.2rem; box-shadow: 0 2px 4px rgba(0,0,0,0.2); background-color: #dc3545; border: 1px solid #dc3545; color: #fff; line-height: 1.5; text-align: center; cursor: pointer;">
                        <i class="bi bi-x-lg"></i>
                    </button>
                    
                    <!-- 圖片 Label -->
                    <label for="${uniqueId}" style="cursor: pointer; display: block; width: 100%; height: 100%;">
                        <img src="${imgSrc}" data-role="preview-img" alt="圖片預覽" 
                             style="width: 100%; height: 100%; object-fit: cover; border-radius: 0.375rem; border: 3px solid white; background-color: #e9ecef;"
                             onerror="this.onerror=null; this.src=${placeholderImg};">
                    </label>
                    
                    <!-- 隱藏的檔案上傳 input，使用 data-role 屬性供 JS 選取 -->
                    <input type="file" accept="image/*" id="${uniqueId}" data-role="file-input" style="display: none;" name="new_prod_img">
                </div>
            `;
  };

  const createNewDeleteCard = () => {
    // 新增一個記錄刪除圖片的input
    return `
            <input type="text" class="form-control" value="" name="delete-item" id="delete-item-input" hidden></td>
            `;
  };

  if (type == "edit") {
    imageContainer.insertAdjacentHTML("beforeend", createNewDeleteCard());
    arr.forEach(function (product, index) {
      console.log(product.image_url);
      imageContainer.insertAdjacentHTML(
        "beforeend",
        createNewImageCard(product.image_url, product.image_id)
      );
    });
  }

  // 監聽 "新增圖片" 按鈕的點擊事件
  addImageBtn.addEventListener("click", () => {
    imageContainer.insertAdjacentHTML("beforeend", createNewImageCard());
  });

  // --- 使用事件委派 (Event Delegation) 處理動態新增的元素 ---

  // 監聽容器內的點擊事件 (用於刪除)
  imageContainer.addEventListener("click", (event) => {
    // 透過 .closest() 和 data-action 屬性判斷點擊的是否為刪除按鈕
    const deleteButton = event.target.closest('[data-action="delete"]');
    if (deleteButton) {
      // 如果是，則找到最近的卡片父元素並將其從 DOM 中移除
      if (type == "edit") {
        const cardDiv = deleteButton.closest("div");
        const id = cardDiv.dataset.id;
        const deleteValue = document.getElementById("delete-item-input").value;
        document.getElementById("delete-item-input").value =
          id + "," + deleteValue;
      }
      deleteButton.closest('[data-role="image-card"]').remove();
    }
  });

  // 監聽容器內的 change 事件 (用於檔案選擇)
  imageContainer.addEventListener("change", (event) => {
    if (type == "edit") {
      const cardDiv = event.target.closest("div");
      const id = cardDiv.dataset.id;
      const deleteValue = document.getElementById("delete-item-input").value;
      document.getElementById("delete-item-input").value =
        id + "," + deleteValue;
    }
    // 透過 .matches() 和 data-role 屬性判斷觸發事件的是否為檔案輸入框
    if (event.target.matches('[data-role="file-input"]')) {
      const fileInput = event.target;
      const file = fileInput.files[0];

      if (file) {
        // 找到該卡片內的 img 元素來顯示預覽
        const previewImage = fileInput
          .closest('[data-role="image-card"]')
          .querySelector('[data-role="preview-img"]');

        const reader = new FileReader();
        reader.onload = (e) => {
          previewImage.src = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    }
  });
}

function skuEdit(type, id = 1, arr = []) {
  const addSkuBtn = document.getElementById("add-sku-btn");
  const skuTbody = document.getElementById("sku-tbody");
  let skuItemPrefix = "XXXX-";
  if (type == "new") {
    skuItemPrefix = generateCodeFromInteger(maxId + 1) + "-";
  } else {
    skuItemPrefix = generateCodeFromInteger(id) + "-";
  }
  let skuCount = skuTbody.childElementCount;

  const createNewSku = (code = "", price = 0, stock = 0) => {
    // 使用時間戳和隨機數確保 ID 的唯一性
    skuCount = skuTbody.childElementCount;
    return `
              <tr data-role="sku-card">
                  <td>
                  <div class="input-group">
                  <span class="input-group-text">${skuItemPrefix}</span>
                  <input type="text" class="form-control" value="${code}" placeholder="WHI-M" name="sku-item-code[${skuCount}]" required></td>
                  </div>
                  <td><input type="number" class="form-control" value="${price}" placeholder="499" name="sku-item-price[${skuCount}]" required></td>
                  <td><input type="number" class="form-control" value="${stock}" placeholder="150" name="sku-item-stock[${skuCount}]" required></td>
                  <td><button class="btn btn-sm btn-outline-danger" data-action="delete"><i class="bi bi-x-circle"></i></button>
                  </td>
              </tr>
            `;
  };

  if (type == "edit") {
    arr.forEach(function (sku, index) {
      // console.log(product.image_url);
      skuTbody.insertAdjacentHTML(
        "beforeend",
        createNewSku(sku.sku_code, sku.price, sku.stock_quantity)
      );
    });
  }

  // 監聽 "新增sku" 按鈕的點擊事件
  addSkuBtn.addEventListener("click", () => {
    skuTbody.insertAdjacentHTML("beforeend", createNewSku());
  });

  // --- 使用事件委派 (Event Delegation) 處理動態新增的元素 ---

  // 監聽tbody內的點擊事件 (用於刪除)
  skuTbody.addEventListener("click", (event) => {
    // 透過 .closest() 和 data-action 屬性判斷點擊的是否為刪除按鈕
    const deleteButton = event.target.closest('[data-action="delete"]');
    if (deleteButton) {
      // 如果是，則找到最近的卡片父元素並將其從 DOM 中移除
      deleteButton.closest('[data-role="sku-card"]').remove();
    }
  });
}

/**
 * 將產品資料與分類資料合併的函式
 * @param {Array} products 產品陣列
 * @param {Array} categories 分類陣列
 * @returns {Array} 回傳已合併分類名稱的產品陣列
 */
function mergeProductWithCategoryName(products, categories) {
  // 1. 建立一個分類的查找表 (Lookup Table)，這裡使用 Map 物件
  const categoryMap = new Map(
    categories.map((category) => [category.cate_id, category.cate_name])
  );

  // 2. 遍歷產品陣列，並為每個產品加上 cate_name 屬性
  return products.map((product) => {
    return {
      ...product, // 使用展開運算子保留原始產品所有屬性
      cate_name: categoryMap.get(product.prod_cate_id) || "未分類", // 透過 Map 快速查找，如果找不到則給予預設值
    };
  });
}

async function saveProd(event) {
  event.preventDefault();

  //取得表單資料
  const form = event.target;
  const formData = new FormData(form);
  try {
    //呼叫儲存API
    const newProducts = await saveProduct(formData);
    window.alert("儲存資料成功");
    loadPage("./feature/product/products");
  } catch (error) {
    console.log("儲存資料失敗");
    console.log("錯誤訊息" + error.message);
    window.alert("儲存失敗" + error.message);
  }
}

/**
 * reset 編輯表單
 */

function resetEditForm() {
  const prodEditForm = document.getElementById("product-edit-form");
  prodEditForm.reset();

  const imageContainer = document.getElementById("imageContainer");
  imageContainer.innerHTML = "";

  const skuTbody = document.getElementById("sku-tbody");
  skuTbody.innerHTML = "";
  const addSkuBtn = document.getElementById("add-sku-btn");
  const addImageBtn = document.getElementById("addImageBtn");

  removeAllListenersFromElement(skuTbody);
  removeAllListenersFromElement(addSkuBtn);
  removeAllListenersFromElement(imageContainer);
  removeAllListenersFromElement(addImageBtn);
}

function removeAllListenersFromElement(element) {
  let clone = element.cloneNode(true);
  element.replaceWith(clone);
}

/**
 * 初始化頁面
 * export default => 會在第一次載入時執行一次
 * @description 取得資料、建立 DOM 並綁定事件
 */
async function init() {
  try {
    /**
     * 載入分類樹狀圖
     */

    // 取得分類資料
    const categories = await fetchCategoriesProd();

    console.log(categories);

    // 使用資料建立樹狀結構
    const treeData = buildCategoryTree(categories);

    // 生成樹狀結構的 HTML
    const treeHTML = generateTreeHTML(treeData);

    // 將生成的 HTML 插入到容器中
    const container = document.getElementById("product-category-tree");
    container.innerHTML = treeHTML;

    // 分類樹狀圖點擊事件
    container.addEventListener("click", function (event) {
      event.preventDefault();
      const targetLi = event.target.closest("li");
      if (targetLi.dataset) {
        // 從 li 元素中的 dataset 中讀取分類資訊
        const { id, name, isParent } = targetLi.dataset;
        // 填充右側查詢表單
        cateEditFilter(id, name, isParent);
      }
    });

    // 生成編輯表單中父分類的下拉選單
    const select = document.querySelector("select[name = 'prod-cate-select']");
    select.innerHTML = '<option value="0" disabled>預設分類（不可選）</option>';
    // 為每個類別生成option並append到select之下
    categories.forEach((cat) => {
      if (!cat.is_parent) {
        const option = document.createElement("option");
        option.value = cat.cate_id;
        option.textContent = `${cat.cate_name} (ID: ${cat.cate_id})`;
        select.appendChild(option);
      }
    });

    /**
     * 初始化商品列表
     */
    searchProd();

    document
      .getElementById("clear-cate-btn")
      .addEventListener("click", setCateDefault);
    document.getElementById("new-prod-btn").addEventListener("click", newProd);
    document
      .getElementById("search-prod-form")
      .addEventListener("submit", searchProd);
    document
      .getElementById("product-edit-form")
      .addEventListener("submit", saveProd);
  } catch (error) {
    //初始化頁面錯誤處理
    console.error("初始化商品頁面時發生錯誤:", error);
    document.getElementById(
      "product-main-section"
    ).innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
  }
}

/**
 * 搜尋商品事件
 */
async function searchProd(event = null) {
  let newProducts;
  if (event != null) {
    console.log("event!=null");

    event.preventDefault();
    //取得表單資料
    const form = event.target;
    const formData = new FormData(form);
    const prodData = Object.fromEntries(formData.entries());
    if (
      prodData["prod-cate-id"] === "null" ||
      prodData["prod-cate-id"] === ""
    ) {
      prodData["prod-cate-id"] = null; // 轉換為真正的 null
    }
    if (prodData["prod-status"] === "null" || prodData["prod-status"] === "") {
      prodData["prod-status"] = null; // 轉換為真正的 null
    }
    console.log(prodData);

    newProducts = await fetchProducts(prodData);
  } else {
    newProducts = await fetchProducts();
  }

  try {
    //呼叫查詢API
    const newCategories = await fetchCategoriesProd();
    const searchResult = mergeProductWithCategoryName(
      newProducts,
      newCategories
    );

    // 建立一個自訂事件
    const searchEvent = new CustomEvent("update-products", {
      detail: {
        searchResult: searchResult,
      },
    });
    // 在 window 上廣播這個事件
    window.dispatchEvent(searchEvent);
  } catch (error) {
    console.log("搜尋資料失敗");
    console.log("錯誤訊息" + error.message);
  }
}

init();
