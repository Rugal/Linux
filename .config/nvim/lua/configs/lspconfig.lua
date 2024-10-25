-- EXAMPLE
local on_attach = require("nvchad.configs.lspconfig").on_attach
local on_init = require("nvchad.configs.lspconfig").on_init
local capabilities = require("nvchad.configs.lspconfig").capabilities

local lspconfig = require "lspconfig"
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

-- lsps with default config
for _, lsp in ipairs(servers) do
  lspconfig[lsp].setup {
    on_attach = on_attach,
    on_init = on_init,
    capabilities = capabilities,
  }
end
