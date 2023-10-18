local options = {
  fillchars = "eob: ,fold: ,foldopen:,foldsep: ,foldclose:",

  ff="unix",

  list = true,
  listchars = "eol:↴,tab:>-,trail:~,extends:>,precedes:<",

  -- relativenumber = true,
  -- termguicolors = true,
}

vim.opt.shortmess:append "c"

for k, v in pairs(options) do
  vim.opt[k] = v
end
