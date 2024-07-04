return {
  {
    "stevearc/conform.nvim",
    -- event = 'BufWritePre', -- uncomment for format on save
    config = function()
      require "configs.conform"
    end,
  },

  -- These are some examples, uncomment them if you want to see them work!
  {
    "neovim/nvim-lspconfig",
    config = function()
      require("nvchad.configs.lspconfig").defaults()
      require "configs.lspconfig"
    end,
  },

  {
  	"williamboman/mason.nvim",
  	opts = {
  		ensure_installed = {
        "lua-language-server",
        "html-lsp",
        "clojure-lsp",
        "jdtls",
        "jedi-language-server",
        "kotlin-language-server",
        "yaml-language-server",
        "eslint-lsp",
        "stylua"
  		},
  	},
  },

  {
  	"nvim-treesitter/nvim-treesitter",
  	opts = {
  		ensure_installed = {
        "java", "kotlin", "clojure", "yaml", "xml",
  			"vim", "lua", "vimdoc",
       "html", "css"
  		},
  	},
  },
}
