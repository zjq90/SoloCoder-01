export const mockData = {
  homeStats: {
    transactionAmount: 1256800.50,
    profitAmount: 25680.80,
    directMerchants: 45,
    agentMerchants: 128,
    totalAgents: 56,
    products: 12,
    machines: 256
  },

  transactionList: [
    { id: 1, merchantName: '张三便利店', amount: 1250.00, time: '2026-04-25 14:30', type: '微信支付' },
    { id: 2, merchantName: '李四超市', amount: 3680.00, time: '2026-04-25 13:20', type: '支付宝' },
    { id: 3, merchantName: '王五水果店', amount: 560.00, time: '2026-04-25 12:15', type: '微信支付' },
    { id: 4, merchantName: '赵六餐厅', amount: 8900.00, time: '2026-04-25 11:45', type: '银行卡' },
    { id: 5, merchantName: '孙七服装店', amount: 2350.00, time: '2026-04-25 10:30', type: '微信支付' }
  ],

  profitList: [
    { id: 1, merchantName: '张三便利店', amount: 12.50, time: '2026-04-25 14:30', rate: '0.01' },
    { id: 2, merchantName: '李四超市', amount: 36.80, time: '2026-04-25 13:20', rate: '0.01' },
    { id: 3, merchantName: '王五水果店', amount: 5.60, time: '2026-04-25 12:15', rate: '0.01' },
    { id: 4, merchantName: '赵六餐厅', amount: 89.00, time: '2026-04-25 11:45', rate: '0.01' },
    { id: 5, merchantName: '孙七服装店', amount: 23.50, time: '2026-04-25 10:30', rate: '0.01' }
  ],

  merchantList: [
    { id: 1, name: '张三便利店', type: '直属', phone: '138****1234', status: '正常', createTime: '2026-01-15' },
    { id: 2, name: '李四超市', type: '直属', phone: '139****5678', status: '正常', createTime: '2026-02-20' },
    { id: 3, name: '王五水果店', type: '代理', phone: '137****9012', status: '正常', createTime: '2026-03-10' },
    { id: 4, name: '赵六餐厅', type: '代理', phone: '136****3456', status: '正常', createTime: '2026-03-15' },
    { id: 5, name: '孙七服装店', type: '直属', phone: '135****7890', status: '正常', createTime: '2026-04-01' }
  ],

  agentList: {
    direct: [
      { id: 1, name: '一级代理A', phone: '138****1111', level: '一级', merchants: 15, machines: 50, status: '正常' },
      { id: 2, name: '一级代理B', phone: '138****2222', level: '一级', merchants: 23, machines: 86, status: '正常' },
      { id: 3, name: '一级代理C', phone: '138****3333', level: '一级', merchants: 8, machines: 32, status: '正常' }
    ],
    indirect: [
      { id: 101, name: '二级代理A1', phone: '139****4444', level: '二级', parent: '一级代理A', merchants: 5, machines: 20, status: '正常' },
      { id: 102, name: '二级代理B1', phone: '139****5555', level: '二级', parent: '一级代理B', merchants: 12, machines: 45, status: '正常' }
    ]
  },

  productList: [
    { id: 1, name: '标准POS机', code: 'POS-001', price: 299.00, commission: '0.01', count: 156, status: '在售' },
    { id: 2, name: '智能POS机', code: 'POS-002', price: 599.00, commission: '0.012', count: 85, status: '在售' },
    { id: 3, name: '扫码盒子', code: 'BOX-001', price: 199.00, commission: '0.008', count: 200, status: '在售' },
    { id: 4, name: '云音箱', code: 'SPEAKER-001', price: 159.00, commission: '0.006', count: 120, status: '在售' }
  ],

  machineList: [
    { id: 1, sn: 'SN2026040001', product: '标准POS机', status: '已激活', merchant: '张三便利店', bindTime: '2026-04-01' },
    { id: 2, sn: 'SN2026040002', product: '智能POS机', status: '已激活', merchant: '李四超市', bindTime: '2026-04-05' },
    { id: 3, sn: 'SN2026040003', product: '扫码盒子', status: '已激活', merchant: '王五水果店', bindTime: '2026-04-10' },
    { id: 4, sn: 'SN2026040004', product: '标准POS机', status: '未激活', merchant: '-', bindTime: '-' },
    { id: 5, sn: 'SN2026040005', product: '云音箱', status: '已激活', merchant: '赵六餐厅', bindTime: '2026-04-15' }
  ],

  userInfo: {
    name: '张总',
    phone: '138****8888',
    avatar: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20business%20avatar%20portrait%20man%20suit%20red%20tie&image_size=square',
    level: '一级代理'
  },

  accountInfo: {
    totalAmount: 125680.50,
    availableAmount: 85680.50,
    pendingAmount: 20000.00,
    withdrawnAmount: 20000.00
  },

  accountDetailList: [
    { id: 1, type: '分润收入', amount: 1250.00, balance: 85680.50, time: '2026-04-25 14:30', remark: '张三便利店交易分润' },
    { id: 2, type: '提现', amount: -5000.00, balance: 84430.50, time: '2026-04-24 10:00', remark: '提现到银行卡' },
    { id: 3, type: '分润收入', amount: 3680.00, balance: 89430.50, time: '2026-04-23 16:45', remark: '李四超市交易分润' },
    { id: 4, type: '分润收入', amount: 560.00, balance: 85750.50, time: '2026-04-22 12:30', remark: '王五水果店交易分润' }
  ],

  withdrawList: [
    { id: 1, amount: 5000.00, status: '已完成', bank: '招商银行', account: '****8888', time: '2026-04-24 10:00' },
    { id: 2, amount: 10000.00, status: '已完成', bank: '招商银行', account: '****8888', time: '2026-04-18 15:30' },
    { id: 3, amount: 5000.00, status: '处理中', bank: '招商银行', account: '****8888', time: '2026-04-25 09:00' }
  ],

  profitTotal: 25680.80,
  profitMonthTotal: 8560.50,

  profitDetailList: [
    { id: 1, merchantName: '张三便利店', amount: 125.00, time: '2026-04-25 14:30', rate: '0.01', transactionAmount: 12500.00 },
    { id: 2, merchantName: '李四超市', amount: 368.00, time: '2026-04-25 13:20', rate: '0.01', transactionAmount: 36800.00 },
    { id: 3, merchantName: '王五水果店', amount: 56.00, time: '2026-04-25 12:15', rate: '0.01', transactionAmount: 5600.00 },
    { id: 4, merchantName: '赵六餐厅', amount: 890.00, time: '2026-04-25 11:45', rate: '0.01', transactionAmount: 89000.00 },
    { id: 5, merchantName: '孙七服装店', amount: 235.00, time: '2026-04-25 10:30', rate: '0.01', transactionAmount: 23500.00 }
  ]
}

export function formatAmount(amount) {
  return amount.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}
