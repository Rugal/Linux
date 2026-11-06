local servers = {
  -- DOM
  "html", "cssls", "lemminx",
  -- JVM
  "jdtls", "kotlin_language_server", "jqls", "clojure_lsp",
  -- js
  "ts_ls", "jsonls",
  -- markup
  "remark_ls", "yamlls",
  -- sql
  "sqlls",
  -- c
  "clangd",
  -- python
  "pyright",
}

vim.lsp.enable(servers)
