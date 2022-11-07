local fn = vim.fn

-- Automatically install packer
local install_path = fn.stdpath "data" .. "/site/pack/packer/start/packer.nvim"
if fn.empty(fn.glob(install_path)) > 0 then
  PACKER_BOOTSTRAP = fn.system 
  "git",
  "clone",
  "--depth",
  "1",
  "https://github.com/bthomason/packer.nvim",
  install_path,

  print "Installing packer close and reopen Neovim..."
  vim.cmd [[packadd packer.nvim]]
end

-- Autocommand that reloads neovim henever you save the plugins.lua file
vim.cmd [[
augroup packer_user_config
autocmd!
autocmd BufWritePost plugin.lua source <afile> | PackerSync
augroup end
]]

-- Use a protected call so we don't error out on first use
local status_ok, packer = pcall(require, "packer")
if not status_ok then
  return
end

-- Have packer use a popup window
packer.init {
  display = {
    open_fn = function()
      return require("packer.util").float { border = "rounded" }
    end,
  },
}

-- Install your plugins here
return packer.startup(function(use)
  ------------------------------------
  -- package management
  ------------------------------------
  use "wbthomason/packer.nvim" -- Have packer manage itself

  ------------------------------------
  -- basic dependency
  ------------------------------------
  use {
    'lewis6991/impatient.nvim',
    config = function()
      require('impatient')
    end
  }
  use "nvim-lua/popup.nvim" -- An implementation of the Popup API from vim in Neovim
  use "nvim-lua/plenary.nvim" -- Useful lua functions used ny lots of plugins
  use "kyazdani42/nvim-web-devicons"

  ------------------------------------
  -- Colorschemes
  ------------------------------------
  use "ishan9299/nvim-solarized-lua"
  use 'folke/tokyonight.nvim'

  ------------------------------------
  -- welcome page
  ------------------------------------
  use {
    'goolord/alpha-nvim',
    config = function ()
      require'alpha'.setup(require'alpha.themes.startify'.config)
    end
  }

  ------------------------------------
  -- file explorer
  ------------------------------------
  use {
    "nvim-neo-tree/neo-tree.nvim",
    branch = "v2.x",
    requires = {
      "MunifTanjim/nui.nvim",
    },
    config = function()
      require("fileexplorer")
    end

  }

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
  use {
    'simrat39/symbols-outline.nvim',
    config = function()
      require("outline")
    end
  }

  ------------------------------------
  -- language service provider
  ------------------------------------
  -- gD    go to declaration
  -- gd    go to definition
  -- K     show signature
  -- gr    show reference
  use {
    "williamboman/nvim-lsp-installer",
    {
      "neovim/nvim-lspconfig",
      config = function()
        require("language")
      end
    }
  }

  -- hover information
  -- K toggle
  use {
    "lewis6991/hover.nvim",
    config = function()
      require("hover").setup {
        init = function()
          -- Require providers
          require("hover.providers.lsp")
          -- require('hover.providers.gh')
          -- require('hover.providers.jira')
          require('hover.providers.man')
          require('hover.providers.dictionary')
        end,
        preview_opts = {
          -- border = nil
          border = 'single'
        },
        -- Whether the contents of a currently open hover window should be moved
        -- to a :h preview-window when pressing the hover keymap.
        preview_window = false,
        title = true
      }

      -- Setup keymaps
      vim.keymap.set("n", "K", require("hover").hover, {desc = "hover.nvim"})
      -- vim.keymap.set("n", "gK", require("hover").hover_select, {desc = "hover.nvim (select)"})
    end
  }

  -- syntax highlight
  use {
    'nvim-treesitter/nvim-treesitter',
    run = ':TSUpdate',
    config = function()
      require("tsconfig")
    end
  }

  -- todo highlight
  -- :TodoTrouble
  -- :TodoLocList
  -- :TodoTelescope
  use {
    "folke/todo-comments.nvim",
    requires = {
      "tpope/vim-repeat",
    },
    config = function()
      require("todo-comments").setup {
      }
    end
  }

  ------------------------------------
  -- auto-completion
  ------------------------------------
  use "hrsh7th/nvim-cmp"
  use 'hrsh7th/cmp-nvim-lsp'
  use 'hrsh7th/cmp-buffer'
  use 'hrsh7th/cmp-path'
  use 'hrsh7th/cmp-cmdline'
  use 'f3fora/cmp-spell'

  ------------------------------------
  -- git
  ------------------------------------
  -- <leader>hp: preview hunk
  -- <leader>hs: stage hunk
  -- <leader>hu: unstage hunk
    -- map({'n', 'v'}, '<leader>hs', ':Gitsigns stage_hunk<CR>')
    -- map({'n', 'v'}, '<leader>hr', ':Gitsigns reset_hunk<CR>')
    -- map('n', '<leader>hS', gs.stage_buffer)
    -- map('n', '<leader>hu', gs.undo_stage_hunk)
    -- map('n', '<leader>hR', gs.reset_buffer)
    -- map('n', '<leader>hp', gs.preview_hunk)
    -- map('n', '<leader>hb', function() gs.blame_line{full=true} end)
    -- map('n', '<leader>tb', gs.toggle_current_line_blame)
    -- map('n', '<leader>hd', gs.diffthis)
    -- map('n', '<leader>hD', function() gs.diffthis('~') end)
    -- map('n', '<leader>td', gs.toggle_deleted)

    -- Text object
    -- map({'o', 'x'}, 'ih', ':<C-U>Gitsigns select_hunk<CR>')

  use {
    'lewis6991/gitsigns.nvim',
    tag = 'release', -- To use the latest release
    requires = {
      "tpope/vim-repeat",
    },
    config = function()
      require("git")
    end
  }

  ------------------------------------
  -- status line
  ------------------------------------
  use {
    'feline-nvim/feline.nvim',
    config = function()
      require('feline').setup{}
    end
  }

  ------------------------------------
  -- buffer
  ------------------------------------
  use {
    'akinsho/bufferline.nvim',
    tag = "v2.*",
    config = function()
      require("bufferline").setup{}
      --[[ diagnostics_indicator = function(count, level, diagnostics_dict, context)
        local icon = level:match("error") and " " or " "
        return " " .. icon .. count
      end ]]
      diagnostics_indicator = function(count, level, diagnostics_dict, context)
        local s = " "
        for e, n in pairs(diagnostics_dict) do
          local sym = e == "error" and " "
          or (e == "warning" and " " or "" )
          s = s .. n .. sym
        end
        return s
      end
    end
  }

  -- close buffer
  -- CTRL x
  use {
    "kazhala/close-buffers.nvim",
    config = function()
      require('close_buffers').setup({
        preserve_window_layout = { 'this' },
        next_buffer_cmd = function(windows)
          require('bufferline').cycle(1)
          local bufnr = vim.api.nvim_get_current_buf()

          for _, window in ipairs(windows) do
            vim.api.nvim_win_set_buf(window, bufnr)
          end
        end,
      })
    end
  }

  ------------------------------------
  -- parenthesis
  ------------------------------------
  -- change surrounding character
  -- ys{motion}{char}
  -- ds{char}
  -- cs{target}{replacement}
  use({
    "kylechui/nvim-surround",
    tag = "*", -- Use for stability; omit to use `main` branch for the latest features
    config = function()
      require("nvim-surround").setup({
        -- Configuration here, or leave empty to use defaults
      })
    end
  })

  -- rainbow parenthesis
  use {
    "p00f/nvim-ts-rainbow",
    config = function()
      require("nvim-treesitter.configs").setup {
        rainbow = {
          enable = true,
          extended_mode = true, -- Also highlight non-bracket delimiters like html tags, boolean or table: lang -> boolean
          max_file_lines = nil, -- Do not enable for files with more than n lines, int
        }
      }
    end
  }

  -- auto close
  use 'm4xshen/autoclose.nvim'

  ------------------------------------
  -- file search
  ------------------------------------
  -- :Telescope
  use {
    'nvim-telescope/telescope.nvim',
    config = function()
      require('telescope').setup {
        extensions = {
          fzf = {
            fuzzy = true,                    -- false will only do exact matching
            override_generic_sorter = true,  -- override the generic sorter
            override_file_sorter = true,     -- override the file sorter
            case_mode = "smart_case",        -- or "ignore_case" or "respect_case"
            -- the default case_mode is "smart_case"
          }
        }
      }
    end
  }

  -- native fuzzy find
	use {
    'nvim-telescope/telescope-fzf-native.nvim',
    run = 'cmake -S. -Bbuild -DCMAKE_BUILD_TYPE=Release && cmake --build build --config Release && cmake --install build --prefix build'
  }

  ------------------------------------
  -- lint
  ------------------------------------
  use {
    "mfussenegger/nvim-lint",
    config = function()
      require('lint').linters_by_ft = {
          python = {"pycodestyle",},
          cpp = {"cppcheck",}
      }
    end
  }

  ------------------------------------
  -- command assistance
  ------------------------------------
  use {
    "folke/which-key.nvim",
    config = function()
      require("which-key").setup {
        -- your configuration comes here
        -- or leave it empty to use the default settings
        -- refer to the configuration section below
      }
    end
  }

  ------------------------------------
  -- other
  ------------------------------------
  use {
    "yamatsum/nvim-cursorline",
    config = function()
      require('nvim-cursorline').setup {
        cursorline = {
          enable = true,
          timeout = 1000,
          number = false,
        },
        cursorword = {
          enable = true,
          min_length = 3,
          hl = { underline = true },
        }
      }
    end
  }

  -- trouble
  -- :TroubleToggle
  use {
    "folke/trouble.nvim",
    config = function()
      require("trouble").setup {
        -- your configuration comes here
        -- or leave it empty to use the default settings
        -- refer to the configuration section below
      }
    end
  }

  -- indentation
  use {
    "lukas-reineke/indent-blankline.nvim",
    config = function()
      require("indent_blankline").setup {
        -- for example, context is off by default, use this to turn it on
        space_char_blankline = " ",
        show_current_context = true,
        show_current_context_start = true,
      }
    end
  }

  -- comment
  -- use 'b3nj5m1n/kommentary'
  -- gcc     line-wise comment
  -- gbc     block-wise comment
  use {
    'numToStr/Comment.nvim',
    config = function()
      require('Comment').setup()
    end
  }

  -- Automatically set up your configuration after cloning packer.nvim
  -- Put this at the end after all plugins
  if PACKER_BOOTSTRAP then
    require("packer").sync()
  end
end)
