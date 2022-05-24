local cmp = require "cmp"

cmp_config = {
  formatting = {
    fields = { "kind", "abbr", "menu" },
    max_width = 0,
    kind_icons = {
      Class = " ",
      Color = " ",
      Constant = "ﲀ ",
      Constructor = " ",
      Enum = "練",
      EnumMember = " ",
      Event = " ",
      Field = " ",
      File = "",
      Folder = " ",
      Function = " ",
      Interface = "ﰮ ",
      Keyword = " ",
      Method = " ",
      Module = " ",
      Operator = "",
      Property = " ",
      Reference = " ",
      Snippet = " ",
      Struct = " ",
      Text = " ",
      TypeParameter = " ",
      Unit = "塞",
      Value = " ",
      Variable = " ",
    },
    source_names = {
      nvim_lsp = "(LSP)",
      treesitter = "(TS)",
      path = "(Path)",
      buffer = "(Buffer)",
      spell = "(Spell)",
    },
    duplicates = {
      buffer = 1,
      path = 1,
      nvim_lsp = 0,
      luasnip = 1,
    },
    duplicates_default = 0,
    format = function(entry, vim_item)
      local max_width = cmp_config.formatting.max_width
      if max_width ~= 0 and #vim_item.abbr > max_width then
        vim_item.abbr = string.sub(vim_item.abbr, 1, max_width - 1) .. "…"
      end
      vim_item.kind = cmp_config.formatting.kind_icons[vim_item.kind]
      vim_item.menu = cmp_config.formatting.source_names[entry.source.name]
      vim_item.dup = cmp_config.formatting.duplicates[entry.source.name]
      or cmp_config.formatting.duplicates_default
      return vim_item
    end,
  },
  window = {
    completion = cmp.config.window.bordered(),
    documentation = cmp.config.window.bordered(),
  },

  sources = {
    { name = "nvim_lsp" },
    { name = "treesitter" },
    { name = "path" },
    { name = "buffer" },
    { name = "spell" },
  },
  mapping = cmp.mapping.preset.insert {
    ["<C-k>"] = cmp.mapping.select_prev_item(),
    ["<C-j>"] = cmp.mapping.select_next_item(),
    ["<C-d>"] = cmp.mapping.scroll_docs(-4),
    ["<C-f>"] = cmp.mapping.scroll_docs(4),
    -- TODO: potentially fix emmet nonsense
    ["<Tab>"] = cmp.mapping(function(fallback)
      if cmp.visible() then
        cmp.select_next_item()
      else
        fallback()
      end
    end, {
    "i",
    "s",
  }),
      ["<S-Tab>"] = cmp.mapping(function(fallback)
        if cmp.visible() then
          cmp.select_prev_item()
        else
          fallback()
        end
      end, {
      "i",
      "s",
    }),

    ["<C-p>"] = cmp.mapping.complete(),
    ["<C-e>"] = cmp.mapping.abort(),
  },
}

cmp.setup(cmp_config)
