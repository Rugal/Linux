local overrides = require("custom.configs.overrides")

---@type NvPluginSpec[]
local plugins = {

  -- Override plugin definition option

  {
    "neovim/nvim-lspconfig",
    dependencies = {
      -- format & linting
      {
        "jose-elias-alvarez/null-ls.nvim",
        config = function()
          require "custom.configs.null-ls"
        end,
      },
    },
    config = function()
      require "plugins.configs.lspconfig"
      require "custom.configs.lspconfig"
    end, -- Override to setup mason-lspconfig
  },

  -- override plugin configs
  {
    "williamboman/mason.nvim",
    opts = overrides.mason
  },

  {
    "nvim-treesitter/nvim-treesitter",
    opts = overrides.treesitter,
  },

  {
    "nvim-tree/nvim-tree.lua",
    opts = overrides.nvimtree,
  },

  -- Install a plugin
  {
    "max397574/better-escape.nvim",
    event = "InsertEnter",
    config = function()
      require("better_escape").setup()
    end,
  },

  -- todo highlight
  -- :TodoTrouble
  -- :TodoLocList
  -- :TodoTelescope
  {
    "folke/todo-comments.nvim",
    lazy = false,
    dependencies = {
      -- format & linting
      {
        "tpope/vim-repeat",
      },
    },
    config = function()
      require("todo-comments").setup {}
    end
  },

  -- trouble
  -- :TroubleToggle
  {
    "folke/trouble.nvim",
    lazy = false,
    config = function()
      require("trouble").setup {
        -- your configuration comes here
        -- or leave it empty to use the default settings
        -- refer to the configuration section below
      }
    end
  },

  ------------------------------------
  -- code outline
  ------------------------------------
  -- F12
  -- K : toggle preview
  -- h : fold
  -- l : unfold
  -- E : unfold all
  -- W : fold all
  -- a : code action
  -- o : focus
  -- <CR> : goto
  {
    'simrat39/symbols-outline.nvim',
    lazy = false,
    config = function()
      require("symbols-outline").setup {
        highlight_hovered_item = true,
        show_guides = true,
        auto_preview = true,
        position = 'right',
        relative_width = true,
        width = 25,
        auto_close = false,
        show_numbers = true,
        show_relative_numbers = true,
        show_symbol_details = true,
        preview_bg_highlight = 'Pmenu',
        autofold_depth = 2,
        auto_unfold_hover = false,
        fold_markers = { '', '' },
        wrap = false,
        symbols = {
          File = { icon = "", hl = "@text.uri" },
          Module = { icon = "󱒌", hl = "@namespace" },
          Namespace = { icon = "", hl = "@namespace" },
          -- Package = { icon = "", hl = "@namespace" },
          Class = { icon = "", hl = "@type" },
          Method = { icon = "󰡱", hl = "@method" },
          Property = { icon = "󰽐", hl = "@method" },
          Field = { icon = "", hl = "@field" },
          -- Constructor = { icon = "", hl = "@constructor" },
          Enum = { icon = "", hl = "@type" },
          -- Interface = { icon = "", hl = "@type" },
          Function = { icon = "󰊕", hl = "@function" },
          Variable = { icon = "󰫧", hl = "@constant" },
          -- Constant = { icon = "", hl = "@constant" },
          String = { icon = "󰅳", hl = "@string" },
          -- Number = { icon = "#", hl = "@number" },
          -- Boolean = { icon = "⊨", hl = "@boolean" },
          Array = { icon = "", hl = "@constant" },
          -- Object = { icon = "⦿", hl = "@type" },
          Key = { icon = "", hl = "@type" },
          -- Null = { icon = "NULL", hl = "@type" },
          -- EnumMember = { icon = "", hl = "@field" },
          Struct = { icon = "", hl = "@type" },
          Event = { icon = "", hl = "@type" },
          -- Operator = { icon = "+", hl = "@operator" },
          -- TypeParameter = { icon = "𝙏", hl = "@parameter" },
          Component = { icon = "󰡀", hl = "@function" },
          Fragment = { icon = "󰽏", hl = "@field" },

        },
        keymaps = { -- These keymaps can be a string or a table for multiple keys
          close = {"<Esc>", "q"},
          goto_location = "<Cr>",
          focus_location = "o",
          hover_symbol = "<C-space>",
          toggle_preview = "K",
          rename_symbol = "r",
          code_actions = "a",
          fold = "h",
          unfold = "l",
          fold_all = "W",
          unfold_all = "E",
          fold_reset = "R",
        },
      }
    end
  },

  -- To make a plugin not be loaded
  -- {
  --   "NvChad/nvim-colorizer.lua",
  --   enabled = false
  -- },

  -- All NvChad plugins are lazy-loaded by default
  -- For a plugin to be loaded, you will need to set either `ft`, `cmd`, `keys`, `event`, or set `lazy = false`
  -- If you want a plugin to load on startup, add `lazy = false` to a plugin spec, for example
  -- {
  --   "mg979/vim-visual-multi",
  --   lazy = false,
  -- }
}

return plugins
