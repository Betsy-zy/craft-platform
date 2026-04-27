#!/usr/bin/env python3
# 快速添加作品脚本（Python版）
# 注意：运行此脚本前需要确保前端开发服务器正在运行

import os
import random
import requests
from pathlib import Path

# API基础URL
API_BASE_URL = 'http://localhost:5173/api'

# 素材文件夹路径
MATERIALS_DIR = Path('c:\\Users\\ZY\\Desktop\\4c\\素材')

# 匠人映射表，用于将素材文件夹名称映射到系统中的匠人ID
CRAFTSMAN_MAP = {
    '杨师傅': 1,  # 假设杨师傅的ID是1
    '万师傅': 2,  # 假设万师傅的ID是2
    '米师傅': 3,  # 假设米师傅的ID是3
    '张师傅': 4,  # 假设张师傅的ID是4
}

# 作品描述模板
DESCRIPTION_TEMPLATES = [
    '传统手工制作，工艺精湛，独一无二',
    '非遗传承技艺，匠心独运，值得收藏',
    '纯手工打造，细节精致，品质保证',
    '传统与现代结合，既实用又具有艺术价值',
    '采用优质材料，精心制作，经久耐用'
]

# 价格范围
PRICE_RANGE = {
    'min': 100,
    'max': 2000
}

# 库存范围
STOCK_RANGE = {
    'min': 1,
    'max': 50
}

# 生成随机价格
def get_random_price():
    return random.randint(PRICE_RANGE['min'], PRICE_RANGE['max'])

# 生成随机库存
def get_random_stock():
    return random.randint(STOCK_RANGE['min'], STOCK_RANGE['max'])

# 获取随机描述
def get_random_description():
    return random.choice(DESCRIPTION_TEMPLATES)

# 上传图片
def upload_image(file_path):
    try:
        with open(file_path, 'rb') as f:
            files = {'file': f}
            response = requests.post(f'{API_BASE_URL}/works/upload', files=files)
            response.raise_for_status()
            return response.json()
    except Exception as e:
        print(f'上传图片失败: {e}')
        return None

# 创建作品
def create_work(work_data):
    try:
        response = requests.post(f'{API_BASE_URL}/works', json=work_data)
        response.raise_for_status()
        print(f'作品创建成功: {work_data["name"]}')
        return response.json()
    except Exception as e:
        print(f'创建作品失败: {work_data["name"]}, {e}')
        return None

# 处理单个匠人文件夹
def process_craftsman_folder(craftsman_name, folder_path):
    craftsman_id = CRAFTSMAN_MAP.get(craftsman_name)
    if not craftsman_id:
        print(f'未找到匠人ID: {craftsman_name}')
        return

    print(f'开始处理 {craftsman_name} 的作品...')

    # 读取文件夹中的图片文件
    image_files = []
    for file in os.listdir(folder_path):
        file_path = os.path.join(folder_path, file)
        if os.path.isfile(file_path):
            ext = os.path.splitext(file)[1].lower()
            if ext in ['.jpg', '.jpeg', '.png', '.gif']:
                image_files.append(file)

    # 处理每个图片文件
    for image_file in image_files:
        image_path = os.path.join(folder_path, image_file)
        file_name = os.path.splitext(image_file)[0]

        # 生成作品数据
        work_data = {
            'name': file_name,
            'title': file_name,
            'description': get_random_description(),
            'price': get_random_price(),
            'stock': get_random_stock(),
            'craftsmanId': craftsman_id,
            'status': 'active'
        }

        # 上传图片
        print(f'上传图片: {image_file}')
        image_url = upload_image(image_path)
        if not image_url:
            print(f'跳过作品 {file_name}，图片上传失败')
            continue

        # 添加图片URL到作品数据
        work_data['image'] = image_url

        # 创建作品
        create_work(work_data)

    print(f'{craftsman_name} 的作品处理完成')

# 主函数
def main():
    print('开始批量添加作品...')

    # 遍历素材文件夹中的匠人文件夹
    for folder_name in os.listdir(MATERIALS_DIR):
        folder_path = os.path.join(MATERIALS_DIR, folder_name)
        if os.path.isdir(folder_path) and folder_name in CRAFTSMAN_MAP:
            process_craftsman_folder(folder_name, folder_path)

    print('批量添加作品完成！')

if __name__ == '__main__':
    main()
