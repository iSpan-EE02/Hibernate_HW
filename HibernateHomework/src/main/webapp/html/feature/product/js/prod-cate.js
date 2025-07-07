// 將扁平的分類陣列轉換為樹狀結構
export function buildCategoryTree(list) {
  const map = {};
  const roots = [];
  list.forEach((item) => {
    // 假設後端回傳的欄位是 cate_id, parent_cate_id, cate_name, cate_desc
    map[item.cate_id] = { ...item, children: [] };
  });
  list.forEach((item) => {
    if (item.parent_cate_id && map[item.parent_cate_id]) {
      map[item.parent_cate_id].children.push(map[item.cate_id]);
    } else {
      roots.push(map[item.cate_id]);
    }
  });
  return roots;
}

// 遞迴函式：根據樹狀結構生成 HTML
export function generateTreeHTML(nodes) {
  let html = "<ul>";
  for (const node of nodes) {
    const isParent = node.is_parent;
    if (isParent == 0) {
      html += `<li data-id="${node.cate_id}" 
                       data-name="${node.cate_name}" 
                       data-desc="${node.cate_desc}" 
                       data-parent-id="${node.parent_cate_id || 0}"
                       data-is-parent="${isParent}">
                       <a href="#">
                           <i class="bi ${
                             isParent ? "bi-caret-down-fill" : "bi-dash"
                           }"></i>
                           <b>${node.cate_name}</b>
                       </a>`;
    } else {
      html += `<li data-id="${node.cate_id}" 
                       data-name="${node.cate_name}" 
                       data-desc="${node.cate_desc}" 
                       data-parent-id="${node.parent_cate_id || 0}"
                       data-is-parent="${isParent}">
                       <span>
                           <i class="bi ${
                             isParent ? "bi-caret-down-fill" : "bi-dash"
                           }"></i>
                           <b>${node.cate_name}</b>
                       </span>`;
    }
    //  在 li 標籤中，透過 data-is-parent 屬性儲存它是否為父分類的資訊
    if (isParent) {
      html += generateTreeHTML(node.children);
    }
    html += "</li>";
  }
  html += "</ul>";
  return html;
}
