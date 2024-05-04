---@type MappingsTable
local M = {}

M.general = {
  n = {
    [";"] = { ":", "enter command mode", opts = { nowait = true } },
    ["<leader>w"] = {":%s/\\s\\+$//<cr>:let @/=''<CR>", "Clear trailing spaces"},
    ["<F12>"] = { ":SymbolsOutline<CR>", "Open symbol outline", },
    ["<C-S-Left>"] = { ":vertical resize +5<CR>", "Resize +5", },
    ["<C-S-Right>"] = { ":vertical resize -5<CR>", "Resize -5", },
  },
  v = {
    [">"] = { ">gv", "indent"},
  },
}

M.SymbolsOutline = {
  plugin = false,

  n = {
    ["<space>cs"] = { "<esc><cmd>lua require('symbols-outline').toggle_outline()<cr>", "Toggle Symbol Outline", },
  },
}

M.rest = {
  plugin = false,

  n = {
    ["<leader>rr"] = { "<esc><cmd>lua require('rest-nvim.functions').exec('cursor')<cr>", "Run request under the cursor", },
    ["<leader>rl"] = { "<esc><cmd>lua require('rest-nvim.functions').exec('last')<cr>", "Rerun last request", },
  },
}

return M
