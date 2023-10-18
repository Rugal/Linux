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

return M
