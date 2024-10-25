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
        -- jvm
        "clojure-lsp", "jdtls", "kotlin-language-server",
        -- neovim
        "lua-language-server",
        -- DOM
        "html-lsp", "typescript-language-server",
        -- configuration
        "graphql-language-server", "jq-lsp", "json-lsp", "yaml-language-server", "remark-language-server",
        -- C
        "clangd", "cmake-language-server",
         "rust-analyzer", "gopls",
        "pyright"
  		},
  	},
  },

  {
    "nvim-treesitter/nvim-treesitter",
    opts = {
      ensure_installed = {
        -- git
        "git_config", "git_rebase", "gitattributes", "gitignore", "gitcommit",
        -- jvm
        "java", "kotlin", "clojure",
        "python",
        -- DOM
        "html", "javascript", "typescript", "css", "jq",
        -- configuration
        "yaml", "xml", "json", "graphql",
        "tmux",
        "sql",
        "latex",
        "editorconfig",
        -- neovim
        "vim", "lua", "vimdoc"
      },
    },
  },
}
