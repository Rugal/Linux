local opts = { noremap = true, silent = true }

-- Shorten function name
local keymap = vim.api.nvim_set_keymap

--Remap space as leader key
keymap("", "<Space>", "<Nop>", opts)
vim.g.mapleader = " "
vim.g.maplocalleader = " "

-- Modes
--   normal_mode = "n",
--   insert_mode = "i",
--   visual_mode = "v",
--   visual_block_mode = "x",
--   term_mode = "t",
--   command_mode = "c",

-- Normal --
-- Better window navigation
keymap("n", "<C-h>", "<C-w>h", opts)
keymap("n", "<C-j>", "<C-w>j", opts)
keymap("n", "<C-k>", "<C-w>k", opts)
keymap("n", "<C-l>", "<C-w>l", opts)

--strip all trailing whitespace in the current file
keymap("n", "<leader>w", ":%s/\\s\\+$//<cr>:let @/=''<CR>", opts)

-- toggle
keymap("n", "\\", ":Neotree toggle<CR>", opts)
keymap("n", "<F12>", ":SymbolsOutline<CR>", opts)

-- window size
keymap("n", "<C-S-Left>", ":vertical resize +5<CR>", opts)
keymap("n", "<C-S-Right>", ":vertical resize -5<CR>", opts)

-- buffer line
keymap("n", "<tab>", ":BufferLineCycleNext<CR>", opts)
keymap("n", "<s-tab>", ":BufferLineCyclePrev<CR>", opts)

keymap("n", "<C-X>",  ":lua require('close_buffers').delete({type = 'this'})<CR>", opts)
