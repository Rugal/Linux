---@type NvPluginSpec[]
local plugins = {
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

}

return plugins
