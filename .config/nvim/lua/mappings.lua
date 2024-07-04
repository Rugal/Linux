require "nvchad.mappings"

-- add yours here

local map = vim.keymap.set

map("n", ";", ":", { desc = "CMD enter command mode" })
map("i", "jk", "<ESC>")

map("n", "<leader>w", ":%s/\\s\\+$//<cr>:let @/=''<CR>", { desc = "Clear trailing spaces" })

map("n", "<C-S-Left>", ":vertical resize +5<CR>", { desc = "Resize +5" })
map("n", "<C-S-Right>", ":vertical resize -5<CR>", { desc = "Resize -5" })

map("n", "<space>cs", "<esc><cmd>lua require('outline').toggle_outline()<cr>", { desc = "Toggle Symbol Outline" })
