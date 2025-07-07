//alpine.js相關
document.addEventListener("alpine:init", () => {
  Alpine.data("pagination", () => ({
    allProducts: [], // 用於儲存從 API 來的完整商品列表
    currentPage: 1, // 目前頁書預設值
    itemsPerPage: 6, // 2. 設定每頁顯示 6 筆商品

    /**
     * paginatedProducts:根據商品數量動態計算當前總頁數
     */
    get totalPages() {
      return Math.ceil(this.allProducts.length / 6);
    },

    /**
     * paginatedProducts:根據商品數量和目前頁數動態計算當前頁面該顯示的商品
     */
    get paginatedProducts() {
      //當商品總數超過6筆時觸發計算
      if (this.allProducts.length > 6) {
        let start = (this.currentPage - 1) * this.itemsPerPage;
        let end = start + this.itemsPerPage;
        if (end > this.allProducts.length) {
          console.log(this.allProducts.length);
          end = this.allProducts.length;
        }

        console.log("共有" + this.allProducts.slice(start, end));
        console.log(start);
        console.log(end);
        return this.allProducts.slice(start, end);
      } else {
        return this.allProducts;
      }
    },

    goToPage(page) {
      this.currentPage = page;
    },
    previousPage() {
      if (this.currentPage == 1) {
        this.currentPage = this.totalPages;
      } else {
        this.currentPage--;
      }
    },
    nextPage() {
      if (this.currentPage == this.totalPages) {
        this.currentPage = 1;
      } else {
        this.currentPage++;
      }
    },
    init() {
      // 監聽搜尋事件
      window.addEventListener("update-products", (event) => {
        // 4. 更新 allProducts，而不是 paginatedProducts
        console.log("test");
        console.log(event.detail.paginatedProducts);

        // this.allProducts = event.detail.paginatedProducts;
        this.allProducts = event.detail.searchResult;
        this.currentPage = 1;
      });
    },
  }));
});
